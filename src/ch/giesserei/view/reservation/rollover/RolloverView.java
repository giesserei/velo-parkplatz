package ch.giesserei.view.reservation.rollover;

import java.util.Collection;

import ch.giesserei.core.Const;
import ch.giesserei.model.ReservationStellplatz;
import ch.giesserei.resource.AppRes;
import ch.giesserei.view.reservation.ReservationViewHelper;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 * Definiert die View "Reservationen verlängern". In dieser View werden ablaufende
 * Reservationen verlängert.
 * 
 * @author Steffen Förster
 */
@SuppressWarnings("serial")
public class RolloverView  extends CustomComponent{
    
    protected static final String GEN_COL_BEMERKUNG = "bemerkung";
    
    protected static final String GEN_COL_MARK = "mark";
    
	private Table rolloverTable;
	
	private NativeSelect period; 
	
    private Button buttonProcess;
    
    private Button buttonDeselectAll;
    
    private ProgressBar progress;
    
	public RolloverView() {
		initComponents();
	}
	
	public NativeSelect getPeriod() {
        return period;
    }

    public Button getButtonProcess() {
        return buttonProcess;
    }
	
    public Button getButtonDeselectAll() {
        return buttonDeselectAll;
    }

    public ProgressBar getProgress() {
        return progress;
    }

    public Table getRolloverTable() {
		return rolloverTable;
	}

	public void setProcessAction(ClickListener action) {
        this.buttonProcess.addClickListener(action);
    }
	
	public void setDeselectAllAction(ClickListener action) {
        this.buttonDeselectAll.addClickListener(action);
    }

	// ---------------------------------------------------------
	// private section
	// ---------------------------------------------------------
	
	private void initComponents() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSpacing(true);
		
		VerticalLayout vlayout = new VerticalLayout();
		vlayout.setSpacing(true);
		
		HorizontalLayout hLayout = new HorizontalLayout();
		hLayout.setSpacing(true);
		hLayout.addComponent(createPeriodSelect());
		
		this.buttonProcess = new Button(AppRes.getString("btn.process.rollover"));
		hLayout.addComponent(this.buttonProcess);
		hLayout.setComponentAlignment(this.buttonProcess, Alignment.MIDDLE_LEFT);
		this.buttonDeselectAll = new Button(AppRes.getString("btn.deselect.all"));
        hLayout.addComponent(this.buttonDeselectAll);
        hLayout.setComponentAlignment(this.buttonDeselectAll, Alignment.MIDDLE_LEFT);
        
		vlayout.addComponent(hLayout);
		
        this.progress = new ProgressBar(new Float(0.0));
        this.progress.setImmediate(true);
        this.progress.setEnabled(false);
        this.progress.setVisible(false);
        this.progress.setWidth("150px");
        vlayout.addComponent(this.progress);
		
        layout.addComponent(vlayout);
		layout.addComponent(createRolloverTable());
		setCompositionRoot(layout);
	}
	
	private Component createPeriodSelect() {
	    FormLayout layout = new FormLayout(); // Caption erscheint links
	    this.period = new NativeSelect(AppRes.getString("reservation.lb.rollover.period"));
        this.period.setNullSelectionAllowed(false);
        this.period.setImmediate(true);
        for (int i = 2; i <= 5; i++) {
    	    this.period.addItem(i);
            this.period.setItemCaption(i, AppRes.getString("reservation.lb.rollover.period.x", i));
        }
        this.period.select(Const.MONATE_ROLLOVER);
        layout.addComponent(this.period);
        return layout;
	}
	
	private Component createRolloverTable() {
		this.rolloverTable = new Table();	
		this.rolloverTable.setWidth("80%");
		this.rolloverTable.setPageLength(12);
		ReservationViewHelper.addBemerkungColumn(GEN_COL_BEMERKUNG, this.rolloverTable);
		addMark(this.rolloverTable);
		return this.rolloverTable;
	}
	
	private void addMark(final Table table) {
        table.addGeneratedColumn(GEN_COL_MARK, new Table.ColumnGenerator() {
            @Override
            public Object generateCell(final Table source, final Object itemId, final Object columnId) {
                HorizontalLayout layout = new HorizontalLayout();
                final ReservationStellplatz reservation = (ReservationStellplatz) itemId;
                CheckBox checkBox = new CheckBox();
                checkBox.setValue(reservation.isMarkAsRollover());
                
                if (reservation.getMieterPerson() == null) {
                    checkBox.setEnabled(false);
                    checkBox.setDescription(AppRes.getString("reservation.notification.person.nicht.zugeordnet"));
                }
                else {
                    checkBox.setImmediate(true);
                }
                
                layout.addComponent(checkBox);
                
                checkBox.addValueChangeListener(new ValueChangeListener() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        reservation.setMarkAsRollover((Boolean) event.getProperty().getValue()); 
                        
                        // Footer anpassen
                        Collection<ReservationStellplatz> items = 
                                (Collection<ReservationStellplatz>) table.getContainerDataSource().getItemIds();
                        int countMarked = 0;
                        for (ReservationStellplatz r : items) {
                            if (r.isMarkAsRollover()) {
                                countMarked ++;
                            }
                        }
                        table.setColumnFooter(GEN_COL_MARK, AppRes.getString("reservation.table.row.count.marked", countMarked));
                    }
                });
                return layout;
            }
        });
    }
	
}
