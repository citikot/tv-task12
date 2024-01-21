package net.thumbtack.school.database.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class School {

    private int id;
    private String name;
    private int year;
    private List<Group> groups;

    public School() {}

    public School(int id, String name, int year, List<Group> groups) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.groups = groups;
    }

    public School(int id, String name, int year) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.groups = new ArrayList<>();
    }

    public School(String name, int year) {
        this.id = 0;
        this.name = name;
        this.year = year;
        this.groups = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        School school = (School) o;
        return getId() == school.getId() && getYear() == school.getYear() && Objects.equals(getName(), school.getName()) && Objects.equals(getGroups(), school.getGroups());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getYear(), getGroups());
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void removeGroup(Group group) {
        groups.remove(group);
    }
}
