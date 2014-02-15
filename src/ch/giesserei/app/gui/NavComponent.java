package ch.giesserei.app.gui;

import ch.giesserei.auth.Auth;
import ch.giesserei.auth.UserRights;
import ch.giesserei.resource.AppRes;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;

/**
 * Definiert die Menübar.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class NavComponent extends CustomComponent {

	private MenuBar menuBar;
	
	private MenuItem menuStammdaten;
	
	private MenuItem menuVelo;
	
	private MenuItem menuAdmin;
	
	private MenuItem itemPerson;
	
	//private MenuItem itemJuristischePerson;
	
	private MenuItem itemObjekt;
	
	private MenuItem itemStellplatz;
	
	private MenuItem itemReservationPosition;
	
	private MenuItem itemReservationAnonym;
	
	private MenuItem itemReservationOverdue;
	
	private MenuItem itemReservationRollover;
	
	private MenuItem itemReservationPersonWhg;
	
	private MenuItem itemInitDb;
	
	private MenuItem itemSyncPersonen;
	
	private MenuItem itemLogout;
	
	/**
	 * Konstruktor.
	 */
	public NavComponent() {
		super();
		initComponents();
	}
	
	public MenuBar getMenuBar() {
		return menuBar;
	}

	public MenuItem getMenuStammdaten() {
		return menuStammdaten;
	}
	
	public MenuItem getMenuVelo() {
		return menuVelo;
	}

	public MenuItem getItemPerson() {
		return itemPerson;
	}
	
	public MenuItem getItemJuristischePerson() {
	    // Brauchen wir zunächst nicht
	    throw new UnsupportedOperationException();
		//return itemJuristischePerson;
	}

	public MenuItem getItemObjekt() {
		return itemObjekt;
	}
	
	public MenuItem getItemStellplatz() {
		return itemStellplatz;
	}
	
	public MenuItem getItemReservationPosition() {
		return itemReservationPosition;
	}
	
	public MenuItem getItemReservationAnonym() {
        return itemReservationAnonym;
    }

    public MenuItem getItemReservationOverdue() {
        return itemReservationOverdue;
    }

    public MenuItem getItemReservationRollover() {
        return itemReservationRollover;
    }
    
    public MenuItem getItemReservationPersonWhg() {
        return itemReservationPersonWhg;
    }

    public MenuItem getItemInitDb() {
        return itemInitDb;
    }
	
	public MenuItem getItemSyncPersonen() {
	    return itemSyncPersonen;
	}

    public MenuItem getItemLogout() {
        return itemLogout;
    }
	
	// ---------------------------------------------------------
	// private section
	// ---------------------------------------------------------

    private void initComponents() {
		this.menuBar = new MenuBar();
		this.menuBar.setWidth(100.0f, Unit.PERCENTAGE);
		
		// Menü Stammdaten
		this.menuStammdaten = this.menuBar.addItem(AppRes.getString("menu.item.stammdaten"), null, null);
		this.itemPerson = this.menuStammdaten.addItem(AppRes.getString("menu.item.personen"), null, null);
		//this.itemJuristischePerson = this.menuStammdaten.addItem(resources.getString("menu.item.juristische.personen"), null ,null);
		this.itemObjekt = this.menuStammdaten.addItem(AppRes.getString("menu.item.objekte"), null, null);
		
		// Menü Velo
		this.menuVelo = this.menuBar.addItem(AppRes.getString("menu.item.velo"), null, null);
		this.itemStellplatz = this.menuVelo.addItem(AppRes.getString("menu.item.stellplatz"), null, null);
		this.menuVelo.addSeparator();
		this.itemReservationPosition = this.menuVelo.addItem(AppRes.getString("menu.item.reservation.position"), null, null);
		this.itemReservationPersonWhg = this.menuVelo.addItem(AppRes.getString("menu.item.reservation.person"), null, null);
		this.menuVelo.addSeparator();
		this.itemReservationAnonym = this.menuVelo.addItem(AppRes.getString("menu.item.reservation.anonym"), null, null);
		this.menuVelo.addSeparator();
		this.itemReservationOverdue = this.menuVelo.addItem(AppRes.getString("menu.item.reservation.overdue"), null, null);
		this.menuVelo.addSeparator();
		this.itemReservationRollover = this.menuVelo.addItem(AppRes.getString("menu.item.reservation.rollover"), null, null);
		
		// Menü Admin
		if (Auth.hasRole(UserRights.ROLE_ADMIN)) {
		    this.menuAdmin = this.menuBar.addItem(AppRes.getString("menu.item.admin"), null, null);
		    this.itemInitDb = this.menuAdmin.addItem(AppRes.getString("menu.item.init.db"), null, null);
		    this.itemSyncPersonen = this.menuAdmin.addItem(AppRes.getString("menu.item.sync.personen"), null, null);
		}
		
		// Logout
		this.itemLogout = this.menuBar.addItem(AppRes.getString("menu.item.logout"), null, null);
		
		setCompositionRoot(this.menuBar);
	}
}
