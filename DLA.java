import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DLA {
	static int click = 0;
	static JButton button_start;
	static JCheckBox checkSpawnOnAttach;

	public static void main(String s[]) {
		JFrame frame = new JFrame("Diffusion Limited Aggregation");
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		Panel graph = new Panel();
		button_start = new JButton();
		button_start.setText("START");
		button_start.setToolTipText("Start or stop the simulation");
		button_start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				click++;
				if (click % 2 != 0) {
					graph.draw = true;
					button_start.setText("STOP");
				} else {
					graph.draw = false;
					button_start.setText("START");
				}
				graph.repaint();
			}
		});
		JButton button_restart = new JButton();
		button_restart.setText("CLEAR");
		button_restart.setToolTipText("Clear all existing particles.");
		button_restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				graph.initial();
				graph.repaint();
			}
		});

		JSlider slider_speed = new JSlider(JSlider.HORIZONTAL, 0, 20, 5);
		slider_speed.setMajorTickSpacing(5);
		slider_speed.setMinorTickSpacing(1);
		slider_speed.setPaintTicks(true);
		// Hastable needed for custom labels
		Hashtable<Integer, JLabel> labelTable_speed = new Hashtable<Integer, JLabel>();
		labelTable_speed.put(Integer.valueOf(slider_speed.getMinimum()), new JLabel("Faster"));
		labelTable_speed.put(Integer.valueOf(slider_speed.getMaximum()), new JLabel("Slower"));
		slider_speed.setLabelTable(labelTable_speed);
		slider_speed.setPaintLabels(true);
		slider_speed.setToolTipText(
				"Change the the counter value in the main step() loop, effectively making the simulation go faster.");
		slider_speed.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e2) {
				graph.speed = slider_speed.getValue();
				button_start.setEnabled(true);
			}
		});

		JSlider slider_frequency = new JSlider(JSlider.HORIZONTAL, 1, 100, 25);
		slider_frequency.setMajorTickSpacing(25);
		slider_frequency.setMinorTickSpacing(5);
		slider_frequency.setPaintTicks(true);
		Hashtable<Integer, JLabel> labelTable_frequency = new Hashtable<Integer, JLabel>();
		labelTable_frequency.put(Integer.valueOf(slider_frequency.getMinimum()), new JLabel("More"));
		labelTable_frequency.put(Integer.valueOf(slider_frequency.getMaximum()), new JLabel("Less"));
		slider_frequency.setLabelTable(labelTable_frequency);
		slider_frequency.setPaintLabels(true);
		slider_frequency.setToolTipText("Change how many particles are spawned each step.");
		slider_frequency.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e3) {
				graph.frequency = slider_frequency.getValue();
			}
		});

		JSlider slider_bias_x = new JSlider(JSlider.HORIZONTAL, -25, 25, 0);
		slider_bias_x.setMajorTickSpacing(25);
		slider_bias_x.setMinorTickSpacing(5);
		slider_bias_x.setSnapToTicks(true);
		slider_bias_x.setPaintTicks(true);
		Hashtable<Integer, JLabel> labelTable_bias_x = new Hashtable<Integer, JLabel>();
		labelTable_bias_x.put(Integer.valueOf(slider_bias_x.getMinimum()), new JLabel("X- Bias"));
		labelTable_bias_x.put(Integer.valueOf(slider_bias_x.getMaximum()), new JLabel("X+ Bias"));
		slider_bias_x.setLabelTable(labelTable_bias_x);
		slider_bias_x.setPaintLabels(true);
		slider_bias_x.setToolTipText("Change the particle bias in the X dimension.");
		slider_bias_x.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e4) {
				graph.bias_x = 0.01 * (double) slider_bias_x.getValue();
			}
		});

		JSlider slider_bias_y = new JSlider(JSlider.HORIZONTAL, -25, 25, 15);
		slider_bias_y.setMajorTickSpacing(25);
		slider_bias_y.setMinorTickSpacing(5);
		slider_bias_x.setSnapToTicks(true);
		slider_bias_y.setPaintTicks(true);
		slider_bias_y.setToolTipText("Change the particle bias in the Y dimension. Note that negative bias will make the particles go upwards and they will never meet the line.");
		Hashtable<Integer, JLabel> labelTable_bias_y = new Hashtable<Integer, JLabel>();
		labelTable_bias_y.put(Integer.valueOf(slider_bias_y.getMinimum()), new JLabel("Y- Bias"));
		labelTable_bias_y.put(Integer.valueOf(slider_bias_y.getMaximum()), new JLabel("Y+ Bias"));
		slider_bias_y.setLabelTable(labelTable_bias_y);
		slider_bias_y.setPaintLabels(true);
		slider_bias_y.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e4) {
				graph.bias_y = 0.01 * (double) slider_bias_y.getValue();
			}
		});

		JSlider slider_timeout = new JSlider(JSlider.HORIZONTAL, 1, 10, 10);
		slider_timeout.setMajorTickSpacing(5);
		slider_timeout.setMinorTickSpacing(1);
		slider_timeout.setSnapToTicks(true);
		slider_timeout.setPaintTicks(true);
		slider_timeout.setToolTipText(
				"Change the Thread.sleep(x) value, effectively making the simulation faster. This setting is best left untouched as it puts more strain on the CPU.");
		Hashtable<Integer, JLabel> labelTable_timeout = new Hashtable<Integer, JLabel>();
		labelTable_timeout.put(Integer.valueOf(slider_timeout.getMinimum()), new JLabel("1ms"));
		labelTable_timeout.put(Integer.valueOf(slider_timeout.getMaximum()), new JLabel("10ms"));
		slider_timeout.setLabelTable(labelTable_timeout);
		slider_timeout.setPaintLabels(true);
		slider_timeout.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e4) {
				graph.timeout = slider_timeout.getValue();
			}
		});
		checkSpawnOnAttach = new JCheckBox();
		checkSpawnOnAttach.setText("Spawn particle on attach?");
		checkSpawnOnAttach.setToolTipText("Change whenever an additional particle should be spawned every time one gets attached.");

		JButton button_bias = new JButton();
		button_bias.setText("RESET SETTINGS");
		button_bias.setToolTipText("Reset settings initial values.");
		button_bias.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				graph.bias_x = 0.0;
				graph.bias_y = 0.15;
				graph.speed = 5;
				graph.frequency = 25;
				graph.timeout = 10;
				slider_speed.setValue(5);
				slider_frequency.setValue(25);
				slider_timeout.setValue(10);
				slider_bias_x.setValue(0);
				slider_bias_y.setValue(15);
				checkSpawnOnAttach.setSelected(false);
			}
		});

		JPanel panel_b = new JPanel();
		JPanel panel_a = new JPanel();
		JPanel panel_c = new JPanel();
		JPanel panel_d = new JPanel();
		panel_b.setLayout(new GridLayout(2, 1));
		panel_a.setLayout(new GridLayout(3, 1));
		panel_c.setLayout(new GridLayout(2, 2));
		panel_c.setLayout(new GridLayout(2, 2));
		GridLayout panel_d_layout = new GridLayout(5, 1);
		panel_d_layout.setVgap(1);
		panel_d.setLayout(panel_d_layout);
		panel_a.add(button_start);
		panel_a.add(button_restart);
		panel_a.add(button_bias);
		panel_b.add(slider_bias_y);
		panel_b.add(slider_bias_x);
		panel_c.add(slider_timeout);
		panel_c.add(slider_frequency);
		panel_c.add(slider_speed);
		panel_c.add(checkSpawnOnAttach);
		panel_d.add(Panel.label_particles);
		panel_d.add(Panel.label_step);
		panel_d.add(Panel.label_launch);
		panel_d.add(Panel.label_lattice_size);
		panel_d.add(Panel.label_square_size);
		panel.add(panel_a);
		panel.add(panel_b);
		panel.add(panel_c);
		panel.add(panel_d);
		panel.add(graph);
		frame.add(panel);
		panel.setBackground(Color.GRAY);
		frame.setSize(900, 900);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class Panel extends JPanel {
	private static final long serialVersionUID = 1L;
	static JLabel label_particles = new JLabel("  " + "All particles: " + "0  ");
	static JLabel label_step = new JLabel("  " + "Step count: " + "0  ");
	static JLabel label_launch = new JLabel("  " + "Launching point: " + "  ");
	static JLabel label_lattice_size = new JLabel("  " + "Lattice size: " + "  ");
	static JLabel label_square_size = new JLabel("  " + "Square size: " + "  ");

	int launching_point = 2; // height from which we're spawning new particles
	int lattice_size = 235; // lattice dimensions
	int square_size = 3; // particle size

	int speed = 5;
	int frequency = 25;
	int timeout = 10;
	double bias_x = 0.0;
	double bias_y = 0.15;

	int step_count = 0; // iteracja funkcji step();
	int particles = 0; // amount of particles

	int[][] lattice = new int[lattice_size + 1][lattice_size + 1];
	boolean draw = false;

	public Panel() {
		initial();
	}

	public void initial() {
		// clearing the lattice
		for (int[] xy : lattice)
			Arrays.fill(xy, 0);
		// creating the bottom, horizontla line of static particles
		for (int i = 0; i < lattice_size; i++)
			lattice[i][lattice_size - 1] = 2;

		// label cleaning
		step_count = 0;
		particles = 0;
		label_step.setText("  " + "Step count: " + "0  ");
		label_launch.setText("  " + "Launching point: " + launching_point + "  ");
		label_lattice_size.setText("  " + "Lattice size: " + lattice_size + "  ");
		label_square_size.setText("  " + "Square size: " + square_size + "  ");
		// spawn first particle
		spawn_particle(launching_point);
	}

	public Dimension getPreferredSize() {
		return new Dimension(lattice_size * square_size, lattice_size * square_size);
	}

	void step() {
		step_count++;
		label_step.setText("  " + "Step count: " + step_count + "  ");
		if (step_count % frequency == 0)
			spawn_particle(launching_point);
		for (int counter = 0; counter < lattice_size * lattice_size; counter++) {
			int x_old = (int) (lattice_size * Math.random());
			int y_old = (int) (lattice_size * Math.random());
			if (lattice[x_old][y_old] == 1) {
				int x = x_old;
				int y = y_old;
				double losowa = Math.random();
				if (losowa < 0.25 + bias_x) {
					x += 1;
					x = checkBoundary_xy(x, 'x');
				} else if (losowa < 0.5) {
					x -= 1;
					x = checkBoundary_xy(x, 'x');
				} else if (losowa < 0.75 + bias_y) {
					y += 1;
					y = checkBoundary_xy(y, 'y');
				} else {
					y -= 1;
					y = checkBoundary_xy(y, 'y');
				}
				if (lattice[x][y] == 0) {
					lattice[x_old][y_old] = 0;
					lattice[x][y] = 1;
				}
				if (neighbor(x, y, 0, lattice_size, lattice)) {
					// if particle touches a neighbour we spawn a new one
					lattice[x][y] = 2;
					if (DLA.checkSpawnOnAttach.isSelected())
						spawn_particle(launching_point);
				}

				if (checkEndCondition(x, y)) {
					// if particle reaches the launching point
					for (int i = 0; i < lattice_size; i++) {
						for (int j = 0; j < lattice_size; j++) {
							if (lattice[i][j] == 1) {
								lattice[i][j] = 0;
							}
						}
					}
					DLA.button_start.doClick();
				}
			}
			// increase step counter if we want a faster simulation
			counter += speed;
		}
	}

	public void paintComponent(Graphics graf) {
		super.paintComponent(graf);
		for (int i = 0; i < lattice_size; i++) {
			for (int j = 0; j < lattice_size; j++) {
				if (lattice[i][j] == 1) {
					graf.setColor(Color.magenta);
				}
				if (lattice[i][j] == 2) {
					Color switching = new Color(j % 255, 0, 255 - j % 255);
					graf.setColor(switching);
				}
				if (lattice[i][j] > 0) {
					graf.fillRect(i * square_size, j * square_size, square_size, square_size);
				}
			}
		}
		if (draw) {
			step();
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException t) {
				System.out.println("Caught Intterupted Exception");
			}
			repaint();
		}
	}

	void spawn_particle(int y) {
		// spawning particle from a random point across the top
		Random ran = new Random();
		lattice[ran.nextInt(lattice_size)][y] = 1;
		Panel.label_particles.setText("  " + "All Particles: " + particles++ + "  ");
	}

	boolean neighbor(int x, int y, int MIN, int MAX, int[][] lattice) {
		// checking all neighbors for a given particle
		// also (avoiding Out of Bounds)
		int startX = (x - 1 < MIN) ? x : x - 1;
		int startY = (y - 1 < MIN) ? y : y - 1;
		int endX = (x + 1 > MAX) ? x : x + 1;
		int endY = (y + 1 > MAX) ? y : y + 1;
		for (int i = startX; i <= endX; i++) {
			for (int j = startY; j <= endY; j++) {
				// only if the particle is a static one
				if (lattice[i][j] == 2)
					return true;
			}
		}
		return false;
	}

	int checkBoundary_xy(int point, char dim) {
		// boundary conditions - we allow moving along between the sides in X dimension,
		// but not Y dimension
		switch (dim) {
		case 'y':
			if (point == lattice_size)
				return lattice_size - 1;
			else if (point == -1)
				return 0;
			break;
		case 'x':
			if (point == lattice_size)
				return 0;
			else if (point == -1)
				return lattice_size - 1;
			break;
		}
		return point;
	}

	boolean checkEndCondition(int x, int y) {
		// if the newly attached particle reaches the top launching point
		if (lattice[x][y] == 2) {
			if (y <= launching_point) {
				return true;
			}
		}
		return false;
	}
}
