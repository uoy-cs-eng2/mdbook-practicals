package uk.ac.york.cs.eng2.checkinsim.airport;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * Simulates an airport terminal, with a number of self-checkin desks. These
 * desks have customers start checkin processes randomly. They usually complete
 * them, but they may sometimes cancel the process.
 * 
 * The checkin desks are not very reliable: sometimes they simply stop
 * responding, or they may have a hardware issue or may run out of paper. A
 * checkin desk uses a piece of paper per customer, and will give out a "low
 * paper" warning before it completely runs out of paper.
 *
 * This simulator accepts a 'speed up factor', which can be used to uniformly
 * scale all the waits and intervals between events. By default, it's 1 (no
 * scaling).
 */
public class AirportSimulator {

	/* Time between Status events. */
	private static final Duration STATUS_INTERVAL = Duration.ofSeconds(60);

	/* Probability that an unoccupied terminal has a customer start a check-in process in a tick. */
	private static final double CHECKIN_PROBABILITY = 0.05;

	/* Probability that a customer cancels a check-in process in a tick. */
	private static final double CANCEL_PROBABILITY = 0.00125;

	/* Minimum seconds needed for a check-in. */
	private static final int CHECKIN_MINIMUM_SECONDS = 60;

	/* Maximum seconds needed for a check-in. */
	private static final int CHECKIN_MAXIMUM_SECONDS = 300;

	/* Probability that a machine completely breaks down (not sending status updates anymore). */
	private static final double CRASH_PROBABILITY = 0.0001;

	/* Probability that a machine gets stuck (hardware problem). */
	private static final double STUCK_PROBABILITY = 0.0001;

	/* Initial amount of paper, and amount that we refill paper up to. */
	private static final int FULL_PAPER_AMOUNT = 10;

	/* Threshold for reporting that paper is low. */
	private static final int LOW_PAPER_THRESHOLD = 3;
	
	public enum EventType {
		CHECK_IN, CANCELLED, COMPLETED, OUT_OF_ORDER, LOW_PAPER, STATUS;
	}

	public class Terminal {
		private final int id;

		/** Has it completely stopped responding? */
		private boolean crashed = false;

		/** Is it unable to complete checkins, despite sending status heartbeats? */
		private boolean stuck = false;

		/** How many customers' worth of paper is left for printing check-in stubs. */
		private int paperLeft = FULL_PAPER_AMOUNT;

		/*
		 * Indicates when the next Status event should be fired, so long as this machine
		 * isn't broken.
		 */
		private Instant nextStatus;

		/*
		 * If someone is checking in, this indicates when their check-in process will
		 * complete unless the machine breaks down halfway.
		 */
		private Instant checkinEnd;

		public Terminal(int id) {
			this.id = id;
		}

		public int getId() {
			return id;
		}

		public void tick() {
			// If this breaks down, it will simply stop working indefinitely and not send any events
			if (!crashed) {
				crashed = random.nextDouble() < CRASH_PROBABILITY;
				if (crashed) {
					System.out.printf("--> Machine %s broke down!%n", id);
				}
			}
			if (crashed) {
				return;
			}

			if (nextStatus == null || nextStatus.isBefore(Instant.now())) {
				sendStatusUpdate();
			}

			if (!stuck) {
				stuck = random.nextDouble() < STUCK_PROBABILITY;
				if (stuck) {
					fireEvent(EventType.OUT_OF_ORDER, Terminal.this);
				}
			}
			if (!stuck) {
				simulateCheckIn();
			}
		}

		public boolean isCrashed() {
			return crashed;
		}

		public boolean isStuck() {
			return stuck;
		}

		public boolean isDuringCheckin() {
			return checkinEnd != null;
		}

		private void sendStatusUpdate() {
			fireEvent(EventType.STATUS, Terminal.this);
			nextStatus = Instant.now().plus(STATUS_INTERVAL.dividedBy(speedupFactor));
		}

		private void simulateCheckIn() {
			if (checkinEnd == null) {
				if (random.nextDouble() < CHECKIN_PROBABILITY) {
					fireEvent(EventType.CHECK_IN, Terminal.this);
					computeCheckinEnd();
				}
			} else if (paperLeft > 0 && checkinEnd.isBefore(Instant.now())) {
				fireEvent(EventType.COMPLETED, Terminal.this);

				--paperLeft;
				if (paperLeft == 0) {
					// Ran out of paper!
					fireEvent(EventType.OUT_OF_ORDER, Terminal.this);
				} else if (paperLeft <= LOW_PAPER_THRESHOLD) {
					// Low on paper, give warning
					fireEvent(EventType.LOW_PAPER, Terminal.this);
				}

				checkinEnd = null;
			} else if (random.nextDouble() < CANCEL_PROBABILITY) {
				fireEvent(EventType.CANCELLED, Terminal.this);
				checkinEnd = null;
			}
		}

		private void computeCheckinEnd() {
			final int checkinSeconds = CHECKIN_MINIMUM_SECONDS + random.nextInt(CHECKIN_MAXIMUM_SECONDS - CHECKIN_MINIMUM_SECONDS);
			checkinEnd = Instant.now().plus(Duration.ofSeconds(checkinSeconds).dividedBy(speedupFactor));
		}

		/** Simulates someone going to the machine and refilling it. */
		public void fillPaper() {
			final int originalPaper = this.paperLeft;
			this.paperLeft = FULL_PAPER_AMOUNT;
			if (originalPaper == 0 && checkinEnd != null) {
				// Checkin stopped due to no paper: restart it
				computeCheckinEnd();
			}
		}

		/** Simulates someone going to the machine and fixing it. */
		public void fix() {
			if (crashed || stuck) {
				crashed = false;
				stuck = false;
				if (checkinEnd != null) {
					// Checkin stopped to due crash: restart it
					computeCheckinEnd();
				}
			}
		}

		public int getPaperLeft() {
			return paperLeft;
		}
	}

	private final List<Terminal> terminals = new ArrayList<>();
	private final Random random;
	private int speedupFactor = 1;

	public AirportSimulator(int nTerminals, Random rnd) {
		for (int i = 0; i < nTerminals; i++) {
			terminals.add(new Terminal(i));
		}
		this.random = rnd;
	}

	public Terminal getTerminal(int id) {
		return terminals.get(id);
	}

	public Collection<Terminal> getTerminals() {
		return Collections.unmodifiableCollection(terminals);
	}

	public int getSpeedupFactor() {
		return speedupFactor;
	}

	public void setSpeedupFactor(int speedupFactor) {
		if (speedupFactor < 1) {
			throw new IllegalArgumentException("Speedup factor must be >= 1");
		}
		this.speedupFactor = speedupFactor;
	}

	public void tick() {
		for (Terminal terminal : terminals) {
			terminal.tick();
		}
	}

	/**
	 * This method should be overridden if we want to do something more interesting with these events.
	 */
	protected void fireEvent(EventType type, Terminal t) {
		System.out.printf("Terminal %s at time %s: generated event %s%s%n", t.getId(), Instant.now(), type, t.isDuringCheckin() ? " (during check-in)" : "");
	}

}
