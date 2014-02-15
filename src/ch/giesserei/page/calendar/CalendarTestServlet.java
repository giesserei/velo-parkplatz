package ch.giesserei.page.calendar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;

import ch.giesserei.calendar.BuchungMonat;
import ch.giesserei.calendar.BuchungTyp;
import ch.giesserei.calendar.CalendarColors;
import ch.giesserei.calendar.CalendarRenderProvider;
import ch.giesserei.injection.Injection;

import com.google.inject.Inject;

/**
 * Dieses Servlet dient dem Testen des Buchungskalenders.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
@WebServlet(value = "/kalender", asyncSupported = false)
public class CalendarTestServlet extends HttpServlet {
    
    @Inject
    private CalendarRenderProvider calendarProvider;
    
    public CalendarTestServlet() {
        Injection.injectMembers(this);
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("image/svg+xml; charset=UTF-8");
        
        int monthBefore = 0;
        int monthCount = 15;
        int monthColumns = 5;
        int monthsRows = calcRows(monthColumns, monthCount);
        int scale = 120;
        
        List<BuchungMonat> months = createMonatsListe(monthBefore, monthCount);
        
        this.calendarProvider.writeCalendar(months, monthColumns, monthsRows, new CalendarColors(), scale, resp.getOutputStream());
    }
        
    // ------------------------------------------------------
    // private section
    // ------------------------------------------------------
    
    private int calcRows(int monthColumns, int monthCount) {
        int rows = (int) (monthCount / monthColumns);
        rows += (monthCount % monthColumns > 0) ? 1 : 0;
        return rows;        
    }  
    
    private List<BuchungMonat> createMonatsListe(int monthBefore, int monthCount) {
        List<BuchungMonat> monthList = new ArrayList<BuchungMonat>();
        int offSet = 0 - monthBefore;
        
        monthList.add(new BuchungMonat(DateUtils.addMonths(new Date(), offSet++)));
        monthList.add(new BuchungMonat(DateUtils.addMonths(new Date(), offSet++)));
        monthList.add(new BuchungMonat(DateUtils.addMonths(new Date(), offSet++)));     
        monthList.add(createMonth1(offSet++));
        monthList.add(new BuchungMonat(DateUtils.addMonths(new Date(), offSet++)));
        monthList.add(createMonth2(offSet++));
        monthList.add(createMonth3(offSet++));
        
        for(int i = offSet; i <= (monthCount - monthBefore - 1); i++) {
            monthList.add(new BuchungMonat(DateUtils.addMonths(new Date(), i)));
        }
        
        return monthList;
    }
    
    private BuchungMonat createMonth1(int addMonth) {
        Date date = DateUtils.addMonths(new Date(), addMonth);
        BuchungMonat month = new BuchungMonat(date);
        
        month.addArrivalDay(DateUtils.setDays(date, 2), BuchungTyp.BOOKED);
        month.addDepartureDay(DateUtils.setDays(date, 13), BuchungTyp.BOOKED);
        
        for (int i = 3; i <= 12; i++) {
            month.addOccupiedDay(DateUtils.setDays(date, i), BuchungTyp.BOOKED);
        }
        
        return month;
    }
    
    private BuchungMonat createMonth2(int addMonth) {
        Date date = DateUtils.addMonths(new Date(), addMonth);
        BuchungMonat month = new BuchungMonat(date);
        
        month.addArrivalDay(DateUtils.setDays(date, 4), BuchungTyp.RESERVED);
        month.addDepartureDay(DateUtils.setDays(date, 20), BuchungTyp.RESERVED);
        
        for (int i = 5; i <= 19; i++) {
            month.addOccupiedDay(DateUtils.setDays(date, i), BuchungTyp.RESERVED);
        }
        
        return month;
    }
    
    private BuchungMonat createMonth3(int addMonth) {
        Date date = DateUtils.addMonths(new Date(), addMonth);
        BuchungMonat month = new BuchungMonat(date);
        
        month.addArrivalDay(DateUtils.setDays(date, 10), BuchungTyp.MISC);
        month.addDepartureDay(DateUtils.setDays(date, 25), BuchungTyp.MISC);
        
        for (int i = 11; i <= 24; i++) {
            month.addOccupiedDay(DateUtils.setDays(date, i), BuchungTyp.MISC);
        }
        
        return month;
    }
    
}
