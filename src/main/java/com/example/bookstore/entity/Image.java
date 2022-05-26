package com.example.bookstore.entity;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "image", schema = "bookstore")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String fileURL;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column
    private Timestamp downloadStart;
    @Column
    private Timestamp downloadEnd;


    public Image() {

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }



    public Timestamp getDownloadStart() {
        return downloadStart;
    }

    public void setDownloadStart(Timestamp downloadStart) {
        this.downloadStart = downloadStart;
    }

    public Timestamp getDownloadEnd() {
        return downloadEnd;
    }

    public void setDownloadEnd(Timestamp downloadEnd) {
        this.downloadEnd = downloadEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return id == image.id && Objects.equals(fileURL, image.fileURL);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileURL);
    }
}
