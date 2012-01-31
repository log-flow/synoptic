package synopticgwt.client.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import synopticgwt.client.util.TooltipListener;
import synopticgwt.shared.GWTEdge;
import synopticgwt.shared.LogLine;

/**
 * A panel used to display different information about the log. So far, the
 * provided information that the panel displays are lists of log lines and sets
 * of paths. To save screen real estate, the panel switches state based on what
 * is going to be displayed (ex: running showPaths will switch to the path
 * displaying layout of the panel if it isn't already in said state). If the
 * user so chooses, they can toggle between states manually by clicking on the
 * label atop the panel. The state of the table, when being switched to
 * manually, remains the same until the next RPC is initiated. For example, if
 * one views a list of log lines, and then views a set of paths. If the user
 * then clicks on the label to switch back to displaying the list of log lines,
 * they will remain as they were until the next RPC.
 */
public class LogInfoPanel extends VerticalPanel {

    // CSS Attributes of the log info label
    public static final String LOG_INFO_PATHS_CLASS = "log-info-displaying-paths";
    public static final String LOG_INFO_LINES_CLASS = "log-info-displaying-log-lines";
    public static final String LOG_INFO_LABEL_ID = "log-info-label";

    private final Label logInfoLabel;
    private final LogLinesTable logLinesTable;
    private final PathsThroughPartitionsTable pathsThroughPartitionsTable;

    public LogInfoPanel(String width) {
        super();

        logInfoLabel = new Label("Log Lines");
        logLinesTable = new LogLinesTable();
        pathsThroughPartitionsTable = new PathsThroughPartitionsTable();
        this.setWidth(width);
        init();
    }

    /**
     * Sets up the default way to display all of the components.
     */
    private void init() {
        this.add(logInfoLabel);
        this.add(logLinesTable);
        this.add(pathsThroughPartitionsTable);

        TooltipListener
                .setTooltip(
                        logInfoLabel,
                        "Click on a node to view the associated log lines. Shift+Click to select multiple nodes to view paths through nodes.",
                        ModelTab.TOOLTIP_URL);

        // Set up the default CSS attributes for the table
        DOM.setElementAttribute(logInfoLabel.getElement(), "id",
                LOG_INFO_LABEL_ID);
        DOM.setElementAttribute(logInfoLabel.getElement(), "class",
                LOG_INFO_LINES_CLASS);

        this.pathsThroughPartitionsTable.setVisible(false);
    }

    /**
     * Takes a list of log lines and displays them on the panel, line by line.
     * If the state of the log line table is not visible, the panel will switch
     * to accommodate.
     * 
     * @param lines
     */
    public void showLogLines(List<LogLine> lines) {
        this.logLinesTable.showLines(lines);
        if (!logLinesTable.isVisible()) {
            this.toggleLogInfoDisplay();
        }
    }

    /**
     * Takes a set of trace IDs (each mapped to a path), and displays them on
     * the panel. If the state of the paths table is not already visible, the
     * panel will switch to accommodate. TODO: Explain how these will be
     * displayed according to the pathsThroughPartitionsTable object once the
     * full functionality is implemented.
     * 
     * @param paths
     *            Set of trace IDs mapped to specific paths
     */
    public void showPaths(Map<Integer, Set<GWTEdge>> paths) {
        this.pathsThroughPartitionsTable.showPaths(paths);
        if (!pathsThroughPartitionsTable.isVisible()) {
            this.toggleLogInfoDisplay();
        }
    }

    /**
     * Clears the log lines and the paths tables, and set the visibility back to
     * the log lines table (the default).
     */
    public void clearAll() {
        this.clearLogLines();
        this.clearPaths();

        if (!logLinesTable.isVisible()) {
            toggleLogInfoDisplay();
        }
    }

    /**
     * Clears the log lines display from the info table. However, this does not
     * change the state of what is displayed. To clear both the log lines and
     * the paths, while setting the view back to the default state (showing the
     * empty log lines table), use clearAll.
     */
    public void clearLogLines() {
        logLinesTable.clear();
    }

    /**
     * Clears the traces from the paths display
     */
    public void clearPaths() {
        pathsThroughPartitionsTable.clearPaths();
    }

    /**
     * Returns true whenever the state of the log info panel is displaying the
     * paths table.
     */
    public boolean pathsTableVisible() {
        return this.pathsThroughPartitionsTable.isVisible();
    }

    /**
     * Returns true whenever the state of the log info panel is displaying the
     * log lines table.
     */
    public boolean logLinesTableVisible() {
        return this.logLinesTable.isVisible();
    }

    /**
     * Toggles the info display between showing log lines and showing paths
     * through selected partitions.
     */
    private void toggleLogInfoDisplay() {
        if (logLinesTable.isVisible()) {
            logInfoLabel.setText("Paths");
            DOM.setElementAttribute(logInfoLabel.getElement(), "class",
                    LOG_INFO_PATHS_CLASS);
        } else {
            logInfoLabel.setText("Log Lines");
            DOM.setElementAttribute(logInfoLabel.getElement(), "class",
                    LOG_INFO_LINES_CLASS);
        }

        logLinesTable.setVisible(!logLinesTable.isVisible());
        pathsThroughPartitionsTable.setVisible(!pathsThroughPartitionsTable
                .isVisible());
    }
}
