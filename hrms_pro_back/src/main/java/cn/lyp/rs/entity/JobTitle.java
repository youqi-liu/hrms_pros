package cn.lyp.rs.entity;

import cn.lyp.basics.baseClass.LypBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Data
@Accessors(chain = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_job_title")
@TableName("hrms_job_title")
@ApiModel(value = "职称管理")
public class JobTitle extends LypBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "职称名称")
    private String title;

    @ApiModelProperty(value = "职称代码")
    private String code;

    @ApiModelProperty(value = "备注")
    private String remark;
}