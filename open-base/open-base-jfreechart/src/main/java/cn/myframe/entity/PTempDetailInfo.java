package cn.myframe.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="P_TEMP_DETAILINFO")
public class PTempDetailInfo 
{
	/**
	 * 测量时间
	 */
	private Date inspectionTime ;
	
	/**
	 * 测量时间点
	 */
	private int time ;
	
	/**
	 * 体温
	 */
	private Double temperature ;
	
	/**
	 * 体温类型 (1:口温;2:腋温;3:肛温;4:体温不升)
	 */
	private Integer temperatureType ;

	/**
	 * 体温测量类型((1:正常，2：复试体温，3：补测体温)
	 */
	private Integer tempInspectionType ;
	
	/**
	 * 降温后的体温
	 */
	private Double reductionTemperature;
	
	/**
	 * 脉搏
	 */
	private Integer pulse ;
	
	/**
	 * 脉搏短绌   1：是;  0:否
	 */
	private Integer pulseDeficit ;
	
	/**
	 * 呼吸
	 */
	private Integer breathe ;
	
	/**
	 * 是否使用呼吸机 使用呼吸机(1:是 ; 0:否)
	 */
	private Integer breatheType ;
	
	/**
	 * 心率
	 */
    private Integer heartRate ;	
    
    /**
     * 心率检测类型 1:使用心脏搏起器2：补测心率
     */
    private Integer rateInspectionType ;
    
    /**
     * 记录人
     */
    private String recorder ;
    
    /**
     * 记录时间 
     */
    private Date recordDate ;
    
    /**
     * 上注释
     */
    private String comment1 ;
    
    /**
     * 上注释说明 1:入院; 2：出院;3:转入;4:手术;5:转院;6:死亡;7:分娩
     */
    private String comment1Type;
    /**
     * 下注释
     */
    private String comment2 ;
    
    /**
     * 下注释说明  1:拒测; 2:外出; 3:请假
     */
    private String comment2Type;


	public Date getInspectionTime()
	{
		return inspectionTime;
	}

	public void setInspectionTime(Date inspectionHour)
	{
		this.inspectionTime = inspectionHour;
	}

	public Double getTemperature()
	{
		return temperature;
	}

	public void setTemperature(Double temperature)
	{
		this.temperature = temperature;
	}

	public Integer getTemperatureType()
	{
		return temperatureType;
	}

	public void setTemperatureType(Integer temperatureType)
	{
		this.temperatureType = temperatureType;
	}

	public Integer getTempInspectionType()
	{
		return tempInspectionType;
	}

	public void setTempInspectionType(Integer tempInspectionType)
	{
		this.tempInspectionType = tempInspectionType;
	}

	public Integer getPulse()
	{
		return pulse;
	}

	public void setPulse(Integer pulse)
	{
		this.pulse = pulse;
	}

	public Integer getPulseDeficit()
	{
		return pulseDeficit;
	}

	public void setPulseDeficit(Integer pulseDeficit)
	{
		this.pulseDeficit = pulseDeficit;
	}

	public Integer getBreathe()
	{
		return breathe;
	}

	public void setBreathe(Integer breathe)
	{
		this.breathe = breathe;
	}

	public Integer getBreatheType()
	{
		return breatheType;
	}

	public void setBreatheType(Integer breatheType)
	{
		this.breatheType = breatheType;
	}

	public Integer getHeartRate()
	{
		return heartRate;
	}

	public void setHeartRate(Integer heartRate)
	{
		this.heartRate = heartRate;
	}

	public Integer getRateInspectionType()
	{
		return rateInspectionType;
	}

	public void setRateInspectionType(Integer rateInspectionType)
	{
		this.rateInspectionType = rateInspectionType;
	}

	public String getRecorder()
	{
		return recorder;
	}

	public void setRecorder(String recorder)
	{
		this.recorder = recorder;
	}

	public Date getRecordDate()
	{
		return recordDate;
	}

	public void setRecordDate(Date recordDate)
	{
		this.recordDate = recordDate;
	}

	public String getComment1()
	{
		return comment1;
	}

	public void setComment1(String comment1)
	{
		this.comment1 = comment1;
	}

	public String getComment2()
	{
		return comment2;
	}

	public void setComment2(String comment2)
	{
		this.comment2 = comment2;
	}

	public String getComment1Type()
	{
		return comment1Type;
	}

	public void setComment1Type(String comment1Type)
	{
		this.comment1Type = comment1Type;
	}

	public String getComment2Type()
	{
		return comment2Type;
	}

	public void setComment2Type(String comment2Type)
	{
		this.comment2Type = comment2Type;
	}

	@Transient
	public int getTime()
	{
		return time;
	}

	public void setTime(int time)
	{
		this.time = time;
	}

	public Double getReductionTemperature()
	{
		return reductionTemperature;
	}

	public void setReductionTemperature(Double reductionTemperature)
	{
		this.reductionTemperature = reductionTemperature;
	}

	
	
}
