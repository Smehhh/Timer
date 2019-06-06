package frame;

import timer.EDTHalper;
import timer.TimerData;
import timer.TimerThread;

import javax.swing.*;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TimerFrame extends JFrame {
    private JButton startPause;
    private JButton resetButton;
    private JPanel rootPanel;
    private JLabel timerText;
    private TimerThread timerThread;


    {
        setContentPane(rootPanel);
        Dimension size = new Dimension(400, 300);
        setSize(new Dimension(size));
        setMinimumSize(new Dimension(size));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setStop();
        startPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (timerThread == null) {
                    timerThread = new TimerThread(new TimerInvoker());
                    timerThread.start();
                    setActiveState();
                } else if (timerThread.isActive()) {
                    timerThread.start();
                    setActiveState();
                } else {
                    timerThread.stop();
                    setPause();
                }

            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (timerThread != null) {
                    timerThread.stop();
                    timerThread.reset();
                }
                setStop();
            }
        });
    }

    private void setStop() {
        startPause.setText("Start");
        setTime(0);
    }

    private void setPause() {
        startPause.setText("Continue");
        resetButton.setEnabled(true);
    }

    private void setActiveState() {
        startPause.setText("Pause");
    }

    private class TimerInvoker implements TimerData {
        @Override
        public void setTime(long time) {
            EDTHalper.invokeLater(() -> TimerFrame.this.setTime(time), true);
        }

        @Override
        public long getTime() {
            long time[] = new long[1];
            try {
                EDTHalper.invokeAndWait(() -> time[0] = TimerFrame.this.getTime(), true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return time[0];
        }
    }

    public void setTime(long time) {
        long millis = time % 1000;
        time /= 1000;
        long sec = time % 60;
        time /= 60;
        long mins = time % 60;
        time /= 60;
        long hours = time % 100;
        timerText.setText(String.format("%02d:%02d:%02d.%03d", hours, mins, sec, millis));
    }

    public long getTime() {
        String text[] = timerText.getText().split("[:\\.]");
        long time = Integer.parseInt(text[0]) * 60;
        time += Integer.parseInt(text[1]);
        time *= 60;
        time += Integer.parseInt(text[2]);
        time *= 1000;
        time += Integer.parseInt(text[3]);
        return time;
    }
}
