package ch.giesserei.app.gui;

import ch.giesserei.app.Context;
import ch.giesserei.app.command.CommandInitDb;
import ch.giesserei.app.command.CommandLogout;
import ch.giesserei.app.command.CommandMietobjekt;
import ch.giesserei.app.command.CommandPerson;
import ch.giesserei.app.command.CommandReservationAnonym;
import ch.giesserei.app.command.CommandReservationOverdue;
import ch.giesserei.app.command.CommandReservationPersonWhg;
import ch.giesserei.app.command.CommandReservationPosition;
import ch.giesserei.app.command.CommandReservationRollover;
import ch.giesserei.app.command.CommandStellplatz;
import ch.giesserei.app.command.CommandSynchPersonen;
import ch.giesserei.auth.Auth;
import ch.giesserei.auth.UserRights;

/**
 * Erstellt die GUI der Applikation.
 * 
 * @author Steffen Förster
 */
public class GuiBuilder {
	
	/**
	 * Konstruktor.
	 */
	public GuiBuilder() {
	}
	
	public Context createGui() {
		MainComponent mainComp = new MainComponent();
	    Context context = new Context(mainComp);
	    configureNavComp(mainComp, context);
	    
		return context;
	}
	
	// ---------------------------------------------------------
	// private section
	// ---------------------------------------------------------
	
	private void configureNavComp(MainComponent mainComp, Context context) {
		NavComponent navComp = mainComp.getNavComponent();
		navComp.getItemPerson().setCommand(new CommandPerson(context));
		navComp.getItemObjekt().setCommand(new CommandMietobjekt(context));
		navComp.getItemStellplatz().setCommand(new CommandStellplatz(context));
		navComp.getItemReservationPosition().setCommand(new CommandReservationPosition(context));
		navComp.getItemReservationPersonWhg().setCommand(new CommandReservationPersonWhg(context));
		navComp.getItemReservationAnonym().setCommand(new CommandReservationAnonym(context));
		navComp.getItemReservationRollover().setCommand(new CommandReservationRollover(context));
		navComp.getItemReservationOverdue().setCommand(new CommandReservationOverdue(context));
		navComp.getItemLogout().setCommand(new CommandLogout());
		
		if (Auth.hasRole(UserRights.ROLE_ADMIN)) {
		    navComp.getItemInitDb().setCommand(new CommandInitDb());
		    navComp.getItemSyncPersonen().setCommand(new CommandSynchPersonen());
		}
	}
	
}
