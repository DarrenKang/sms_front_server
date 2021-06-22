package ph.sinonet.vg.live.model.sms;


import ph.sinonet.vg.live.model.enums.PlatformStatus;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jay on 7/12/16.
 *
 * product type of FPMS (i.e. RUIBO, BAIBO, etc...)
 *
 */
@Entity
@Table(name = "platform")
public class Platform implements Serializable {

    @Id
    @Column(name = "platform_id", unique = true, nullable = false)
    private String platformId;
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    @Enumerated(value = EnumType.STRING)
    private PlatformStatus status;


    public Platform() {
        this.status = PlatformStatus.ENABLE;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PlatformStatus getStatus() {
        return status;
    }

    public void setStatus(PlatformStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Platform{" +
                "code='" + code + '\'' +
                ", platformId='" + platformId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}

