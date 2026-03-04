package uk.ac.york.cs.eng2.checkinstats.events;

public interface CheckInTopics {
  String TOPIC_CANCELLED = "selfservice-cancelled";
  String TOPIC_CHECKIN = "selfservice-checkin";
  String TOPIC_COMPLETED = "selfservice-completed";
  String TOPIC_OUTOFORDER = "selfservice-outoforder";
  String TOPIC_STATUS = "selfservice-status";
}
