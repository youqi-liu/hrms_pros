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
import java.math.BigDecimal;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_salary_withdrawal")
@TableName("hrms_salary_withdrawal")
@ApiModel(value = "工资提现")
public class SalaryWithdrawal extends LypBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "提现时间")
    private String salaryTime;

    @ApiModelProperty(value = "申请人ID")
    private String userId;

    @ApiModelProperty(value = "申请金额")
    private BigDecimal moneyData;

    @ApiModelProperty(value = "申请人姓名")
    private String userName;

    @ApiModelProperty(value = "月份")
    private String mouth;

    @ApiModelProperty(value = "审核状态")
    private int status;

    @ApiModelProperty(value = "审核人")
    private String auditName;

    @ApiModelProperty(value = "审核时间")
    private String auditTime;
}