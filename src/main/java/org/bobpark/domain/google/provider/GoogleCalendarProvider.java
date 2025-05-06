package org.bobpark.domain.google.provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp.Browser;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import com.malgn.common.exception.ServiceRuntimeException;

import org.bobpark.configure.properties.GoogleProperties;

@Slf4j
public class GoogleCalendarProvider {

    private static final String APPLICATION_NAME = "on-time-e-work";
    private static final List<String> SCOPES = List.of(CalendarScopes.CALENDAR, CalendarScopes.CALENDAR_EVENTS);

    private final GoogleProperties properties;

    private final Calendar googleCalendar;

    public GoogleCalendarProvider(GoogleProperties properties) {

        this.properties = properties;

        RedirectServer redirectServer = new RedirectServer(properties.redirectUrl());

        try (InputStream in = properties.authLocation().getInputStream();) {
            GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(in));

            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, SCOPES)
                    .setDataStoreFactory(new MemoryDataStoreFactory())
                    .setAccessType("offline")
                    .setApprovalPrompt("force")
                    .build();

            LocalServerReceiver serverReceiver =
                new LocalServerReceiver.Builder()
                    .setHost(redirectServer.getDomain())
                    .setPort(redirectServer.getPort())
                    .setCallbackPath(redirectServer.getCallbackUri())
                    .build();

            Browser browser = url -> {
                log.info("Please open the following address in your browser:");
                log.info("  {}", url);
            };

            Credential credential =
                new AuthorizationCodeInstalledApp(flow, serverReceiver, browser)
                    .authorize("userId");

            googleCalendar =
                new Calendar.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(APPLICATION_NAME)
                    .build();

        } catch (IOException | GeneralSecurityException e) {
            throw new ServiceRuntimeException(e);
        }

    }

    public void addEvent(String eventName, LocalDate startDate, LocalDate endDate) {

        Event event = new Event().setSummary(eventName);

        Period period = Period.between(startDate, endDate);

        long plusDays = 0;

        if (period.getDays() > 0) {
            plusDays++;
        }

        EventDateTime start =
            new EventDateTime()
                .setDate(new DateTime(startDate.format(DateTimeFormatter.ISO_DATE)));

        // 종일 이벤트인 경우 end 정각까지로 인식하는거 같음
        EventDateTime end =
            new EventDateTime()
                .setDate(new DateTime(endDate.plusDays(plusDays).format(DateTimeFormatter.ISO_DATE)));

        event.setStart(start).setEnd(end);

        try {
            Event execute = googleCalendar.events().insert(properties.calendarId(), event).execute();

            log.debug("added event: {}", execute.getHtmlLink());
        } catch (IOException e) {
            throw new ServiceRuntimeException(e);
        }

    }

}
