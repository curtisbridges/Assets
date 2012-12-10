package com.curtisbridges.asset.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.RowFilter;
import javax.swing.SpringLayout;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.curtisbridges.asset.Asset;
import com.curtisbridges.asset.AssetConsolidator;
import com.curtisbridges.asset.AssetTreeFactory;
import com.curtisbridges.asset.ConsolidatedAsset;
import com.curtisbridges.asset.events.AssetListener;
import com.curtisbridges.asset.io.CsvAssetWriter;
import com.curtisbridges.asset.io.HtmlAssetWriter;
import com.curtisbridges.asset.io.OpenCsvAssetReader;
import com.curtisbridges.ui.StatusBar;
import com.curtisbridges.ui.util.ScreenUtilities;
import com.curtisbridges.ui.util.SpringUtilities;

@SuppressWarnings("serial")
public class AssetFrame extends JFrame implements AssetListener {
    private File                inputFile;
    private File                outputFile;

    private OpenCsvAssetReader  reader;
    private List<Asset>         assets;

    private static final String ICON = "images/favicon.png";
    private ImageIcon           icon;
    
    private JTextField          reportNameField;
    private JTextField          filterField;
    private JLabel              rowsLabel;
    private JTabbedPane         tabs;
    private JTree               tree;
    private JTable              table;
    private AssetTableModel     assetTableModel;
    private StatusBar           statusBar;

//    private JPopupMenu         popupMenu;

    public AssetFrame() {
        this(null);
    }
    
    public AssetFrame(File file) {
        super("Assets");
        
        icon = new ImageIcon(ICON);
        setIconImage(icon.getImage());
        
        inputFile = file;
        reader = new OpenCsvAssetReader();
        reader.addAssetListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(new AssetMenuBar());
        setSize(800, 600);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        container.add(createNorthPanel(), BorderLayout.NORTH);
        container.add(createCenterPanel(), BorderLayout.CENTER);
        container.add(createSouthPanel(), BorderLayout.SOUTH);
        
        addWindowListener(new OpenWindowListener());
    }

    private JPanel createNorthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new SpringLayout());
        
        filterField = new JTextField(50);
        filterField.addKeyListener(new FilterKeyListener());
        rowsLabel = new JLabel();
        JPanel rightAlignPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightAlignPanel.add(rowsLabel);
        reportNameField = new JTextField(50);        
        
        panel.add(new JLabel("Report name:"));
        panel.add(reportNameField);
        panel.add(new JPanel());
        
        panel.add(new JLabel("Filter:"));
        panel.add(filterField);
        panel.add(rightAlignPanel);
        
        SpringUtilities.makeCompactGrid(panel, 2, 3, 5, 5, 5, 5);

        return panel;
    }

    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        tabs = new JTabbedPane();
        JPanel treePanel = createTreePanel();
        JPanel tablePanel = createTablePanel();
        
        tabs.add("Summary", tablePanel);
        tabs.add("Details", treePanel);
        
        panel.add(tabs, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTreePanel() {
        JPanel panel = new JPanel();
        
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        tree = new JTree(new DefaultMutableTreeNode());
        
        panel.add(new JScrollPane(tree));
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        assetTableModel = new AssetTableModel();
        table = new JTable(assetTableModel);
        table.setShowGrid(true);
        
        AssetTableCellRenderer renderer = new AssetTableCellRenderer();
        table.setDefaultRenderer(Integer.class, renderer);
        table.setDefaultRenderer(String.class, renderer);
        
        panel.add(new JScrollPane(table));

        return panel;
    }

    private JPanel createSouthPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        statusBar = new StatusBar();
        panel.add(statusBar);

        return panel;
    }
    
    private void setRows(int rows) {
        rowsLabel.setVisible(rows > 0);
        rowsLabel.setText(" Rows: " + rows);
    }
    
    private void doSorting() {
        TableRowSorter<AssetTableModel> sorter = new TableRowSorter<AssetTableModel>(assetTableModel);
        RowFilter<AssetTableModel, Object> rf = new RowFilter<AssetTableModel, Object>() {
            @Override
            public boolean include(RowFilter.Entry<? extends AssetTableModel, ? extends Object> entry) {
                return entry.getValue(0).toString().startsWith(filterField.getText());
            }
        };
        sorter.setRowFilter(rf);
        table.setRowSorter(sorter);
    }
    
    private void doOpen() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Comma Seperated Values Files", "csv", "CSV");        
        JFileChooser choice = new JFileChooser();
        choice.setFileFilter(filter);
        choice.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int option = choice.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            inputFile = choice.getSelectedFile();
            if(inputFile != null) {
                reader.openFileByName(inputFile.getAbsolutePath());
                new Thread(reader, "AssetReaderProcess").start();
            }
        }
    }

    private void doExit() {
        System.exit(0);
    }

    private class AssetMenuBar extends JMenuBar {
        public AssetMenuBar() {
            super();

            add(createFileMenu());
//            add(createEditMenu());
            add(createHelpMenu());
        }

        private JMenu createFileMenu() {
            JMenu menu = new JMenu("File");

            JMenuItem item = null;

            item = new JMenuItem("Open", 'O');
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    doOpen();
                }
            });
            menu.add(item);

            item = new JMenu("Export");
            JMenuItem exportCsvItem = new JMenuItem("CSV", 'C');
            exportCsvItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    CsvAssetWriter writer = new CsvAssetWriter();
                    outputFile = createOutputFile(inputFile, getReportText()+".csv");
                    writer.openFile(outputFile.getAbsolutePath());
                    writer.writeAssets(assets);
                }
            });
            item.add(exportCsvItem);
            JMenuItem exportHtmlItem = new JMenuItem("HTML", 'H');
            exportHtmlItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    HtmlAssetWriter writer = new HtmlAssetWriter();
                    outputFile = createOutputFile(inputFile, getReportText()+".html");
                    writer.openFile(outputFile.getAbsolutePath());
                    writer.writeAssets(assets);
                    
                    try {
                        Desktop.getDesktop().open(outputFile);
                    }
                    catch(IOException exc) {
                        exc.printStackTrace();
                    }
                }
            });
            item.add(exportHtmlItem);
            menu.add(item);

            item = new JMenuItem("Exit", 'X');
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    doExit();
                }
            });
            menu.add(item);

            return menu;
        }

        @SuppressWarnings("unused")
        private JMenu createEditMenu() {
            JMenu menu = new JMenu("Edit");

            return menu;
        }

        private JMenu createHelpMenu() {
            JMenu menu = new JMenu("Help");

            JMenuItem item = new JMenuItem("About", 'A');
            item.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog dialog = new AssetAboutBox();
                    dialog.pack();
                    dialog.setVisible(true);
                    ScreenUtilities.centerOnParent(dialog, AssetFrame.this);
                }
            });
            menu.add(item);

            return menu;
        }
    }

    private class FilterKeyListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent arg0) {
            // does nothing
        }

        @Override
        public void keyReleased(KeyEvent arg0) {
            // does nothing
        }

        @Override
        public void keyTyped(KeyEvent arg0) {
            updateFilter();
        }

        private void updateFilter() {
            doSorting();
        }
    }

    @Override
    public void assetParsingStarted() {
        statusBar.setStatusText("Parsing ...");

        JProgressBar bar = statusBar.getProgressBar();
        bar.setVisible(true);
        bar.setIndeterminate(true);
    }

    @Override
    public void assetFound(Asset asset) {
        // does nothing
    }

    @Override
    public void assetParsingCompleted(List<Asset> assets) {
        this.assets = assets;
        
        statusBar.setStatusText("Completed " + assets.size() + " asset properties.");
        
        JProgressBar bar = statusBar.getProgressBar();
        bar.setVisible(false);
        bar.setIndeterminate(false);
        
        tree.setShowsRootHandles(false);
        tree.setModel(new DefaultTreeModel(AssetTreeFactory.createTree(assets)));
        
        List<ConsolidatedAsset> consolidated = AssetConsolidator.process(assets);
        assetTableModel.setAssets(consolidated);
        
        reportNameField.setText(getDefaultReportText());
        reportNameField.selectAll();
        
        setRows(consolidated.size());
        doSorting();
    }

    @Override
    public void assetParsingWarning(String reason) {
        statusBar.setStatusText("Warning: " + reason);
    }

    @Override
    public void assetParsingFailed(Exception exc) {
        JOptionPane.showMessageDialog(this, exc.getMessage(), "Error parsing assets", JOptionPane.ERROR_MESSAGE);
        statusBar.setStatusText("Error: " + exc.getMessage());
        
        JProgressBar bar = statusBar.getProgressBar();
        bar.setIndeterminate(false);
        bar.setVisible(false);
    }
    
    private String getDefaultReportText() {
        if(inputFile != null) {
            String name = inputFile.getName();
            return name.substring(0, name.length()-".csv".length());
        }
        else {
            return "CinchIT Asset Report";
        }
    }
    
    private String getReportText() {
        String text = reportNameField.getText();
        if(text != null && !text.isEmpty()) {
            return text;
        }
        else {
            return "output";
        }
    }
    
    private File createOutputFile(File inputFile, String name) {
        File parent = inputFile.getParentFile();
        File output = new File(parent.getAbsolutePath() + "/" + name);
        try {
            output.createNewFile();
        }
        catch(IOException exc) {
            exc.printStackTrace();
        }
        
        return output;
    }
    
    private class OpenWindowListener extends WindowAdapter {
        @Override
        public void windowOpened(WindowEvent e) {
            if(inputFile == null) {
                doOpen();
            }
            else {
                reader.openFileByName(inputFile.getAbsolutePath());
                new Thread(reader, "AssetReaderProcess").start();
            }
        }
    }
}
