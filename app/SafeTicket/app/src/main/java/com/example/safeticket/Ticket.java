package com.example.safeticket;

import java.util.Date;

public class Ticket {
    // 사용자 ID, 공연이름, 장소, 날짜, 좌석, 시간, 발급처 명
    private String ticketCode;
    private String attendeeId;
    private String eventName;
    private String venue;
    private String eventDate;
    private String eventTime;
    private String ticketIssuer;
    private String paymentTime;

    Ticket(String ticketCode, String eventName, String eventDate)
    {
        this.ticketCode = ticketCode;
        this.eventName = eventName;
        this.eventDate = eventDate;
    }

    Ticket(String ticketCode, String attendeeId, String eventName, String venue,
           String eventDate, String eventTime, String ticketIssuer, String paymentTime)
    {
        this.ticketCode = ticketCode;
        this.attendeeId = attendeeId;
        this.eventName = eventName;
        this.venue = venue;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.ticketIssuer = ticketIssuer;
        this.paymentTime = paymentTime;
    }

    String getTicketCode() { return this.ticketCode; }
    String getAttendeeId() { return this.attendeeId; }
    String getEventName() { return this.eventName; }
    String getVenue() { return this.venue; }
    String getEventDate() { return this.eventDate; }
    String getEventTime() { return this.eventTime; }
    String getTicketIssuer() { return this.ticketIssuer; }
    String getPaymentTime() { return this.paymentTime; }
}
