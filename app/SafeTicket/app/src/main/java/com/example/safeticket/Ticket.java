package com.example.safeticket;

import java.util.Date;

public class Ticket {
    // 사용자 ID, 티켓코드, 공연이름, 장소, 날짜, 좌석, 시간, 발급처 명
    private String userId;
    private String ticketCode;
    private String eventName;
    private String eventLocation;
    private Date eventDate;
    private String seatNumber;
    private String eventTime;
    private String ticketIssuer;

    Ticket(String userId, String ticketCode, String eventName, String eventLocation,
           Date eventDate, String seatNumber, String eventTime, String ticketIssuer)
    {
        this.userId = userId;
        this.ticketCode = ticketCode;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.seatNumber = seatNumber;
        this.eventTime = eventTime;
        this.ticketIssuer = ticketIssuer;
    }

    String getUserId() { return this.userId; }
    String getTicketCode() { return this.ticketCode; }
    String getEventName() { return this.eventName; }
    String getEventLocation() { return this.eventLocation; }
    Date getEventDate() { return this.eventDate; }
    String getSeatNumber() { return this.seatNumber; }
    String getEventTime() { return this.eventTime; }
    String getTicketIssuer() { return this.ticketIssuer; }
}
