package ch.giesserei.injection;

import ch.giesserei.view.jperson.JuristischePersonViewController;
import ch.giesserei.view.objekt.ObjektViewController;
import ch.giesserei.view.person.PersonViewController;
import ch.giesserei.view.reservation.anonym.AnonymViewController;
import ch.giesserei.view.reservation.overdue.OverdueViewController;
import ch.giesserei.view.reservation.personwhg.PersonWhgViewController;
import ch.giesserei.view.reservation.position.PositionViewController;
import ch.giesserei.view.reservation.rollover.RolloverViewController;
import ch.giesserei.view.stellplatz.StellplatzViewController;

import com.google.inject.AbstractModule;

/**
 * Definiert die Einstiegspunkte in die GUI, welche auf die Dependencies Zugriff erhalten sollen.
 * 
 * @author Steffen Förster
 */
public class GuiModule extends AbstractModule {

    /**
     * Konstruktor.
     */
    public GuiModule() {
    }

    @Override
    protected void configure() {
        bind(ObjektViewController.class);
        bind(PersonViewController.class);
        bind(AnonymViewController.class);
        bind(OverdueViewController.class);
        bind(PositionViewController.class);
        bind(RolloverViewController.class);
        bind(PersonWhgViewController.class);
        bind(StellplatzViewController.class);
        bind(JuristischePersonViewController.class);
    }
}
