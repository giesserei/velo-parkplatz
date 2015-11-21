package ch.giesserei.core;

/**
 * Globale Konstanten.
 * 
 * @author Steffen Förster
 */
public class Const {

	public static final int ANZAHL_STELLPLATZ_SEKTOREN = 6;
	
	public static final int MAX_NUMMER_STELLPLATZ = 298;
	
	public static final double KOSTEN_STELLPLATZ_PEDALPARC= 5;
	
	public static final double KOSTEN_STELLPLATZ_SPEZIAL= 7;
	
	public static final int MONATE_PRO_PERIODE = 12;
	
	/**
	 * In der Liste der ablaufenden Reservationen werden die Reservationen angezeigt,
	 * die in {@link MONATE_ROLLOVER} Monaten oder früher ablaufen.
	 */
	public static final int MONATE_ROLLOVER = 2;
	
	public static final int TAGE_ZAHLUNGSFRIST = 30;
	
	public static final String PATH_ETC = "/var/opt/giesserei/etc";
	
}
