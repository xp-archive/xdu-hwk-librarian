package xp.librarian.model.dto;

import java.io.*;
import java.util.*;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xp
 */
@Getter
@Setter
public class Reservation implements Serializable {

    private static final long serialVersionUID = -4293391434805797781L;

    private Integer id;

    private Integer userId;

    private Integer traceId;

    public enum Status {
        WAITING,    // 等待被归还
        ENABLED,    // 已预订
        CANCELED,   // 预订已取消
        ;
    }

    private Status status;

    private Date applyingTime;

    private Date enabledTime;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Reservation)) return false;
        Reservation that = (Reservation) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
