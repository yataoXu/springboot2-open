package cn.myframe.entity;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
@Entity
@Table(name="P_TEMPINFO")
public class PTempInfo 
{
	
	
	/**
	 * 版本号
	 */
	private Integer version ;
	
	/**
	 * 病历
	 */
	
	
	/**
	 * 病区
	 */
	//private InfectedPatch area;
	
	/**
	 * 床号
	 */
	private String badNo;
	
	/**
	 * 测量日期
	 */
	private Date inspectionDate ;
	
	
	private Integer hospitalDays ;
	
	/**
	 * 术后天数
	 */
	private Integer postoperatDays;
	
	/**
	 * 大便次数
	 */
	private Integer poopCount ;
	
	/**
	 * 大便特殊情况 患者无大便，以“0”表示；灌肠后大便以“E”表示，分子记录大便次数，例：1/E表示灌肠后大便1次；0/E表示灌肠后无排便；11/E表示自行排便1次
	 * 
	 */
	private String poopOtherInfo ;
	
	/**
	 * 血压1
	 */
	private String bloodPressure1 ;
	
	/**
	 * 血压2
	 */
	private String bloodPressure2;
	
	/**
	 * 入量
	 */
	private Integer intake;
	
	/**
	 * 出量
	 */
	private Integer output ;
	
	/**
	 * 体重数值
	 */
	private Double weight;
	
	/**
	 * 体重类型   '1':'数值','2':'平车','3':'卧床','4':'抱入','5':'轮椅'
	 */
	private Integer weightType;
	
	/**
	 * 身高
	 */
	private Integer height;
	
	/**
	 * 身高类型
	 */
	private Integer heightType ;
	
	/**
	 * 皮试
	 */
	private String hypoTest ;
	
	/**
	 * 尿量
	 */
	private Integer urineVolume; 
	               
	/**
	 * 输入液量
	 */
	private Integer inputLiquidCount ;
	
	/**
	 * 录入人
	 */
	private String recorder ;
	
	/**
	 * 录入时间
	 */
	private Date recordDate ;
	
	/**
	 * 备注1
	 */
	private String comment1 ;
	
	/**
	 * 备注2
	 */
	private String comment2;
	
	/**
	 * 备注3
	 */
	private String comment3 ;
	

	private Set<PTempDetailInfo> detailInfo=new HashSet<PTempDetailInfo>() ;

   
	/*@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="AREA_ID")
	public InfectedPatch getArea()
	{
		return area;
	}

	public void setArea(InfectedPatch area)
	{
		this.area = area;
	}
    */
	

	public Date getInspectionDate()
	{
		return inspectionDate;
	}

	public void setInspectionDate(Date inspectionDate)
	{
		this.inspectionDate = inspectionDate;
	}

	public Integer getPostoperatDays()
	{
		return postoperatDays;
	}

	public void setPostoperatDays(Integer postoperatDays)
	{
		this.postoperatDays = postoperatDays;
	}

	public Integer getPoopCount()
	{
		return poopCount;
	}

	public void setPoopCount(Integer poopCount)
	{
		this.poopCount = poopCount;
	}

	public String getPoopOtherInfo()
	{
		return poopOtherInfo;
	}

	public void setPoopOtherInfo(String poopOtherInfo)
	{
		this.poopOtherInfo = poopOtherInfo;
	}

	public String getBloodPressure1()
	{
		return bloodPressure1;
	}

	public void setBloodPressure1(String bloodPressure1)
	{
		this.bloodPressure1 = bloodPressure1;
	}

	public String getBloodPressure2()
	{
		return bloodPressure2;
	}

	public void setBloodPressure2(String bloodPressure2)
	{
		this.bloodPressure2 = bloodPressure2;
	}

	public Integer getIntake()
	{
		return intake;
	}

	public void setIntake(Integer intake)
	{
		this.intake = intake;
	}

	public Integer getOutput()
	{
		return output;
	}

	public void setOutput(Integer output)
	{
		this.output = output;
	}

	public Double getWeight()
	{
		return weight;
	}

	public void setWeight(Double weight)
	{
		this.weight = weight;
	}

	public Integer getWeightType()
	{
		return weightType;
	}

	public void setWeightType(Integer weightType)
	{
		this.weightType = weightType;
	}

	public Integer getHeight()
	{
		return height;
	}

	public void setHeight(Integer height)
	{
		this.height = height;
	}

	public Integer getHeightType()
	{
		return heightType;
	}

	public void setHeightType(Integer heightType)
	{
		this.heightType = heightType;
	}

	public String getHypoTest()
	{
		return hypoTest;
	}

	public void setHypoTest(String hypoTest)
	{
		this.hypoTest = hypoTest;
	}

	public Integer getUrineVolume()
	{
		return urineVolume;
	}

	public void setUrineVolume(Integer urineVolume)
	{
		this.urineVolume = urineVolume;
	}

	@Column(name="INPUTLIQUID_COUNT")
	public Integer getInputLiquidCount()
	{
		return inputLiquidCount;
	}

	public void setInputLiquidCount(Integer inputLiquidCount)
	{
		this.inputLiquidCount = inputLiquidCount;
	}

	@Column(updatable=false)
	public String getRecorder()
	{
		return recorder;
	}

	public void setRecorder(String recorder)
	{
		this.recorder = recorder;
	}

	@Column(updatable=false)
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

	public String getComment3()
	{
		return comment3;
	}

	public void setComment3(String comment3)
	{
		this.comment3 = comment3;
	}

	@OneToMany(mappedBy = "tempInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public Set<PTempDetailInfo> getDetailInfo()
	{
		return detailInfo;
	}

	public void setDetailInfo(Set<PTempDetailInfo> detailInfo)
	{
		this.detailInfo = detailInfo;
	}

	@Transient
	public Integer getHospitalDays()
	{
		return hospitalDays;
	}

	public void setHospitalDays(Integer hospitalDays)
	{
		this.hospitalDays = hospitalDays;
	}

	@Transient
	public String getBadNo()
	{
		return badNo;
	}

	public void setBadNo(String badNo)
	{
		this.badNo = badNo;
	}

	@Version
	public Integer getVersion()
	{
		return version;
	}

	public void setVersion(Integer version)
	{
		this.version = version;
	}
}
