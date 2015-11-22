package com.google.devrel.training.conference.form;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import com.google.devrel.training.conference.domain.Conference;

import java.util.Date;
import java.util.List;

/**
 * Created by dheninger on 2015-11-19.
 */
public class ConferenceForm {
    private String name;
    private String description;
    private List<String> topics;
    private String city;
    private Date startDate;
    private Date endDate;
    private int maxAttendees;

    private ConferenceForm(){

    }

    public ConferenceForm(String name, String description, List<String>topics, String city, Date startDate, Date endDate, int maxAttendees){
        this.name=name;
        this.description=description;
        this.topics = topics==null?null: ImmutableList.copyOf(topics);
        this.city = city;
        this.startDate = startDate==null?null:new Date(startDate.getTime());
        this.endDate = endDate==null?null:new Date(endDate.getTime());
        this.maxAttendees = maxAttendees;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTopics() {
        return topics;
    }

    public String getCity() {
        return city;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getMaxAttendees() {
        return maxAttendees;
    }
}
