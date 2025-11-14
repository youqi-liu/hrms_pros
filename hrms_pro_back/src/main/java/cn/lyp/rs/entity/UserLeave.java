package cn.lyp.rs.entity;

import cn.lyp.basics.baseClass.LypBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_user_leave")
@TableName("hrms_user_leave")
@ApiModel(value = "员工请假")
public class UserLeave extends LypBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "请假日期")
    private String leaveDate;

    @ApiModelProperty(value = "请假人ID")
    private String userId;

    @ApiModelProperty(value = "请假人")
    private String userName;

    @ApiModelProperty(value = "请假理由")
    private String leaveReason;

    /**
     * 未审核 已审核 未通过
     */
    @ApiModelProperty(value = "请假状态")
    private String status;

    @ApiModelProperty(value = "审批人")
    private String auditUser;

    @ApiModelProperty(value = "申请时间")
    private String dateTime;
}