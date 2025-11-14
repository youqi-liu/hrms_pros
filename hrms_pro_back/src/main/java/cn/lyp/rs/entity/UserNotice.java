package cn.lyp.rs.entity;

import cn.lyp.basics.baseClass.LypBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a_user_notice")
@TableName("hrms_user_notice")
@ApiModel(value = "公告")
public class UserNotice extends LypBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标题")
    private String title;

    @Length(max = 1024)
    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "发布时间")
    private String sendTime;

    @ApiModelProperty(value = "发布者ID")
    private String pushId;

    @ApiModelProperty(value = "发布者")
    private String pushName;
}