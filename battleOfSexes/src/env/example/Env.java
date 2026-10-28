package example;

import jason.asSyntax.*;
import jason.environment.*;

import java.awt.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Environment for the "better battle of the sexes" with a Swing GUI. */
public class Env extends Environment {

    private Logger logger = Logger.getLogger("battleOfSexes." + Env.class.getName());

    private JTextArea chat;
    private DefaultTableModel schedule;

    @Override
    public void init(String[] args) {
        super.init(args);
        if (!GraphicsEnvironment.isHeadless()) {
            SwingUtilities.invokeLater(this::buildGui);
        }
    }

    private void buildGui() {
        JFrame f = new JFrame("Battle of the sexes - negotiation");
        chat = new JTextArea();
        chat.setEditable(false);
        chat.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        schedule = new DefaultTableModel(new String[] { "Outing", "Husband", "Wife" }, 0);
        JTable table = new JTable(schedule);
        table.setRowHeight(22);

        JScrollPane left = new JScrollPane(chat);
        left.setBorder(BorderFactory.createTitledBorder("Conversation"));
        JScrollPane right = new JScrollPane(table);
        right.setBorder(BorderFactory.createTitledBorder("Agreed plan (who goes where)"));
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setResizeWeight(0.6);
        f.add(split);
        f.setSize(850, 400);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        f.setVisible(true);
    }

    private void log(String line) {
        logger.info(line);
        SwingUtilities.invokeLater(() -> {
            if (chat != null) {
                chat.append(line + "\n");
            }
        });
    }

    private void setCell(int outing, String agent, String activity) {
        SwingUtilities.invokeLater(() -> {
            if (schedule == null) return;
            while (schedule.getRowCount() <= outing) {
                int n = schedule.getRowCount();
                schedule.addRow(new Object[] { n == 0 ? "Tonight" : "Weekend +" + n, "", "" });
            }
            schedule.setValueAt(activity, outing, agent.equals("husband") ? 1 : 2);
        });
    }

    @Override
    public boolean executeAction(String ag, Structure action) {
        String f = action.getFunctor();
        if (f.equals("say") && action.getArity() == 3) {
            String text = action.getTerm(2).toString().replace("\"", "");
            log(String.format("%-8s -> %-8s: %s", action.getTerm(0), action.getTerm(1), text));
        } else if (f.equals("attend") && action.getArity() == 2) {
            try {
                int n = (int) ((NumberTerm) action.getTerm(0)).solve();
                String act = action.getTerm(1).toString();
                setCell(n, ag, act);
                log(ag + " attends '" + act + "' (outing " + n + ")");
            } catch (Exception e) {
                logger.warning("bad attend action: " + e);
                return false;
            }
        } else {
            logger.info("unknown action: " + action);
            return false;
        }
        return true;
    }
}
