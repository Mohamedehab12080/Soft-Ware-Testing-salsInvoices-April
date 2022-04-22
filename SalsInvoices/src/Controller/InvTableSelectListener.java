
package Controller;

import Model.InvLineTableModel;
import Model.InvoiceHeader;
import Model.InvoiceLine;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import view.InvFrame;


public class InvTableSelectListener implements ListSelectionListener
{
      private InvFrame frame;

    public InvTableSelectListener(InvFrame frame) {
        this.frame = frame;
    }
      
    @Override
    public void valueChanged(ListSelectionEvent e) 
    {
        int SelectedInvoiceIndex=frame.getInvHTbl().getSelectedRow();
        System.out.println("Invoice Selected: "+SelectedInvoiceIndex);
        if(SelectedInvoiceIndex !=-1)
        {
        InvoiceHeader selectedInvoice =frame.getInvoicesArray().get(SelectedInvoiceIndex);
        ArrayList<InvoiceLine> Lines=selectedInvoice.getLines();
        InvLineTableModel lineModle=new InvLineTableModel(Lines);
        frame.setLinesArray(Lines);
        frame.getInvLTbl().setModel(lineModle);
        frame.getCustNameLbl().setText(selectedInvoice.getCustomer());
        frame.getInvNumLbl().setText(String.valueOf(selectedInvoice.getNum()));
        frame.getInvDateLbl().setText(InvFrame.dateFormat.format(selectedInvoice.getInvDate()));
        frame.getInvTotalIbl().setText(String.valueOf(selectedInvoice.getInvoiceTotal()));  
        
          }
    }
}
