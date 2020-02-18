package com.example.kashif.android.data;

public class MyDocsDataModel {

    private String name;
    private String path;
    private String size;
    private String date;
    private String numberOfFiles;
    private boolean isChecked;
    private boolean isFile;

    public MyDocsDataModel() {
    }

    public MyDocsDataModel(String name, String path, String size, String date, String numberOfFiles, boolean isChecked, boolean isFile) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.date = date;
        this.numberOfFiles = numberOfFiles;
        this.isChecked = isChecked;
        this.isFile = isFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getNumberOfFiles() {
        return numberOfFiles;
    }

    public void setNumberOfFiles(String numberOfFiles) {
        this.numberOfFiles = numberOfFiles;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }
}
