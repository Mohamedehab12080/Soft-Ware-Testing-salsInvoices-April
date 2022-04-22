package Controller;

import Model.InvLineTableModel;
import Model.InvTableModel;
import Model.InvoiceHeader;
import Model.InvoiceLine;
import view.InvFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import view.InvoiceHeaderDialog;
import view.InvoiceLineDialog;

/**
 *
 * @author DELL
 */
public class InvoiceActionListener implements ActionListener ,ListSelectionListener {

             
    private InvFrame frame;
    private InvoiceHeaderDialog headerDialog;
    private InvoiceLineDialog lineDialog;

    public InvoiceActionListener(InvFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Save Files":
                saveFiles();
                break;

            case "Load Files":
                loadFiles();
                break;

            case "New Invoice":
                createNewInvoice();
                break;

            case "Delete Invoice":
                deleteInvoice();
                break;

            case "New Line":
                createNewLine();
                break;

            case "Delete Line":
                deleteLine();
                break;
            case "newInvOK":
                    NewInvDialogOk();
                    break;
            case"newInvCancel":
               newInvDialogCancel();
                break;
            case "newLineOK":
               newLineOk();     
                break;
            case "newLineCancel":
            newLineCancel();
            break;
        }
    }

    private void loadFiles() {
        JFileChooser fileChooser = new JFileChooser();
        try {
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fileChooser.getSelectedFile();
                Path headerPath = Paths.get(headerFile.getAbsolutePath());
                List<String> headerLines = Files.readAllLines(headerPath);
                ArrayList<InvoiceHeader> invoiceHeaders = new ArrayList<>();
                for (String headerLine : headerLines) {
                    String[] arr = headerLine.split(",");
                    String str1 = arr[0];
                    String str2 = arr[1];
                    String str3 = arr[2];
                    int code = Integer.parseInt(str1);
                    Date invoiceDate = InvFrame.dateFormat.parse(str2);
                    InvoiceHeader header = new InvoiceHeader(code, str3, invoiceDate);
                    invoiceHeaders.add(header);
                }
                frame.setInvoicesArray(invoiceHeaders);
                result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File lineFile = fileChooser.getSelectedFile();
                    Path linePath = Paths.get(lineFile.getAbsolutePath());
                    List<String> LineLines = Files.readAllLines(linePath); 
                    for (String lineLine : LineLines) {
                        String[] arr = lineLine.split(",");
                        String str1 = arr[0]; //invoice Num int 
                        String str2 = arr[1]; //item name String 
                        String str3 = arr[2];// Price Double
                        String str4 = arr[3];//count int 

                        int code = Integer.parseInt(str1);
                        double invPrice = Double.parseDouble(str3);
                        int count = Integer.parseInt(str4);
                        InvoiceHeader inv = frame.getInvoiceObject(code);
                        InvoiceLine invLine = new InvoiceLine(str2, invPrice, count, inv);
                        inv.getLines().add(invLine);

                    }
                }
                InvTableModel headerTableModle=new InvTableModel(invoiceHeaders);
                frame.setHeaderTableModel(headerTableModle);
                frame.getInvHTbl().setModel(headerTableModle);
               System.out.println("Files read");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createNewInvoice() 
    {
           headerDialog=new InvoiceHeaderDialog(frame);
           headerDialog.setVisible(true);
    }

    private void deleteInvoice() 
    {
        int SelectedInvIndex=frame.getInvHTbl().getSelectedRow();
        if(SelectedInvIndex!=-1)
        {
            frame.getInvoicesArray().remove(SelectedInvIndex);
            frame.getHeaderTableModel().fireTableDataChanged();
            
        frame.setLinesArray(null);
        frame.getInvLTbl().setModel(new InvLineTableModel(null));
        frame.getCustNameLbl().setText("");
        frame.getInvNumLbl().setText("");
        frame.getInvDateLbl().setText("");
        frame.getInvTotalIbl().setText("");
        }
    }

    private void createNewLine() 
    {
        lineDialog=new InvoiceLineDialog(frame);
        lineDialog.setVisible(true);
    }

    private void deleteLine()
    {
        int SelectedLineIndex=frame.getInvLTbl().getSelectedRow();
        int SelectedInvoiceIndex=frame.getInvHTbl().getSelectedRow();
        if(SelectedLineIndex!=-1)
        {
            frame.getLinesArray().remove(SelectedLineIndex);
            InvLineTableModel LineTableModel=(InvLineTableModel) frame.getInvLTbl().getModel();
            LineTableModel.fireTableDataChanged();
            frame.getInvTotalIbl().setText(""+frame.getInvoicesArray().get(SelectedInvoiceIndex).getInvoiceTotal());
            frame.getHeaderTableModel().fireTableDataChanged();
            frame.getInvHTbl().setRowSelectionInterval(SelectedInvoiceIndex, SelectedInvoiceIndex);
                    
        }
    }

    private void saveFiles()
    {
        ArrayList<InvoiceHeader> invoiceArray=frame.getInvoicesArray();
        JFileChooser fc=new JFileChooser();
        try {
       int res= fc.showSaveDialog(frame);
        if(res==JFileChooser.APPROVE_OPTION)
        {
            File HeaderFile=fc.getSelectedFile();
            FileWriter hfw=new FileWriter(HeaderFile);
            String headers="";
            String Lines="";
            for (InvoiceHeader inv:invoiceArray)
            {
                headers+=inv.toString();
                headers+="\n";
                for (InvoiceLine line:inv.getLines()) 
                {
                    Lines+=line.toString();
                    Lines+="\n";
                }
            }
            headers.substring(0,headers.length()-1);
            Lines.substring(0,Lines.length()-1);
            res =fc.showSaveDialog(frame);
            File LineFile=fc.getSelectedFile();
            FileWriter lfw=new FileWriter(LineFile);
            hfw.write(headers);
            lfw.write(Lines);
            hfw.close();
            lfw.close();
            
                    
        }
        }catch (Exception e)
        {
            
        }
    }

    private void newInvDialogCancel() 
    {
        headerDialog.setVisible(false);
        headerDialog.dispose();
        headerDialog=null;
    }

 private void NewInvDialogOk() 
   {
        String custName=headerDialog.getCustNameField().getText();
        String date =headerDialog.getInvDateField().getText();
        Date d=new Date();
        try {
            
            d=InvFrame.dateFormat.parse(date);
            
        } catch (Exception e) 
        {
            JOptionPane.showMessageDialog(frame,"<Html><b style=\"color:red\">"+e+"</b></Html>","Error",JOptionPane.ERROR_MESSAGE);
        }
        int invNumber=0;
        for (InvoiceHeader inv: frame.getInvoicesArray())
        {
            if(inv.getNum()>invNumber) 
            {
                invNumber=inv.getNum();
            }
        }
        invNumber++;
        InvoiceHeader inv=new InvoiceHeader(invNumber, custName, d);
        frame.getInvoicesArray().add(inv);
        frame.getHeaderTableModel().fireTableDataChanged();
        headerDialog.dispose();
        headerDialog=null;
                
}

    @Override
    public void valueChanged(ListSelectionEvent e) 
    {
        
    }

    private void newLineOk()
    {
     lineDialog.setVisible(false);
     
     String name=lineDialog.getItemNameField().getText();
     String Count=lineDialog.getItemCountField().getText();
     String price=lineDialog.getItemPriceField().getText();
     int coun=1;
     double cost=1.1;
        try {
            coun=Integer.parseInt(Count);
            cost=Double.parseDouble(price);
        } catch (Exception e) 
        {
            JOptionPane.showMessageDialog(frame, "Can't convert Number or Price","Invalide Number Format",JOptionPane.ERROR_MESSAGE);
        }
        int SelectedInvHeader=frame.getInvHTbl().getSelectedRow();
        if(SelectedInvHeader!=-1)
        {
           InvoiceHeader invHeader= frame.getInvoicesArray().get(SelectedInvHeader);
           InvoiceLine line =new InvoiceLine(name, cost, coun, invHeader);
//           invHeader.getLines().add(line);
           frame.getLinesArray().add(line);
           InvLineTableModel LineTableModel=(InvLineTableModel) frame.getInvLTbl().getModel();
            LineTableModel.fireTableDataChanged();
            frame.getHeaderTableModel().fireTableDataChanged();
        }  
        frame.getInvHTbl().setRowSelectionInterval(SelectedInvHeader, SelectedInvHeader);
     lineDialog.dispose();
     lineDialog=null;
    }
    private void newLineCancel()
    {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog=null;
    }
    
}
