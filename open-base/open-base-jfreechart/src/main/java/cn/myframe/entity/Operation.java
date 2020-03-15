package cn.myframe.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;


@SuppressWarnings("serial")
@Entity
@Table(name = "P_OPERATION")
public class Operation {

	/**
	 * 手术对应的住院流水
	 */

	/**
	 * 手术流水号（自定义）
	 */
	private String operationflow;

	/**
	 * 拟手术ICD9编码
	 */
	private String icdNov;

	/**
	 * 拟行手术名称
	 */
	private String opsNamev;

	/**
	 * 拟手术等級
	 */
	private String opsLevelv;

	/**
	 * 拟切口状态
	 */
	private String cureStatev;

	/**
	 * 拟切口等級
	 */
	private String incisionLevelv;

	/**
	 * 拟手术日期
	 */
	private Date operationdatev;

	/**
	 * 拟麻醉方法
	 */
	private String anaesthesiav;

	/**
	 * 拟麻醉医生
	 */
	private String anaesthesiaDv;

	/**
	 * 拟手术主刀医生
	 */
	private String opsDv;

	/**
	 * 拟I助
	 */
	private String opsFirstAidv;

	/**
	 * 拟II助
	 */
	private String opsSecondAidv;

	/**
	 * 拟手术主要护士
	 */
	private String opsNv;

	/**
	 * 手术ICD9编码
	 */
	private String icdNo;

	/**
	 * 手术名称
	 */
	private String opsName;

	/**
	 * 手术等級
	 */
	private String opsLevel;

	/**
	 * 切口状态
	 */
	private String cureState;

	/**
	 * 切口等級
	 */
	private String incisionLevel;

	/**
	 * 手术日期
	 */
	private Date operationdate;

	/**
	 * 麻醉方法
	 */
	private String anaesthesiad;

	/**
	 * 麻醉医生
	 */
	private String opsd;

	/**
	 * 手术主刀医生
	 */
	private String anaesthesia;

	/**
	 * I助
	 */
	private String opsFirstAid;

	/**
	 * II助
	 */
	private String opsSecondAid;

	/**
	 * 手术主要护士
	 */
	private String opsn;

	/**
	 * 是否是大手术
	 */
	private boolean big;

	private String patientid;
	
	/**
	 * 数据来源
	 */
	private String dataSource;

	/**
	 * 数据来源id
	 */
	private String sourceId;


	public String getOperationflow() {
		return operationflow;
	}

	public void setOperationflow(String operationflow) {
		this.operationflow = operationflow;
	}

	public String getIcdNo() {
		return icdNo;
	}

	public void setIcdNo(String icdNo) {
		this.icdNo = icdNo;
	}

	public String getOpsName() {
		return opsName;
	}

	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}

	public String getOpsLevel() {
		return opsLevel;
	}

	public void setOpsLevel(String opsLevel) {
		this.opsLevel = opsLevel;
	}

	public String getCureState() {
		return cureState;
	}

	public void setCureState(String cureState) {
		this.cureState = cureState;
	}

	public String getIncisionLevel() {
		return incisionLevel;
	}

	public void setIncisionLevel(String incisionLevel) {
		this.incisionLevel = incisionLevel;
	}

	public Date getOperationdate() {
		return operationdate;
	}

	public void setOperationdate(Date operationdate) {
		this.operationdate = operationdate;
	}

	@Column(name="ISBIG")
	public boolean isBig() {
		return big;
	}

	public void setBig(boolean big) {
		this.big = big;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getIcdNov() {
		return icdNov;
	}

	public void setIcdNov(String icdNov) {
		this.icdNov = icdNov;
	}

	public String getOpsNamev() {
		return opsNamev;
	}

	public void setOpsNamev(String opsNamev) {
		this.opsNamev = opsNamev;
	}

	public String getOpsLevelv() {
		return opsLevelv;
	}

	public void setOpsLevelv(String opsLevelv) {
		this.opsLevelv = opsLevelv;
	}

	public String getCureStatev() {
		return cureStatev;
	}

	public void setCureStatev(String cureStatev) {
		this.cureStatev = cureStatev;
	}

	public String getIncisionLevelv() {
		return incisionLevelv;
	}

	public void setIncisionLevelv(String incisionLevelv) {
		this.incisionLevelv = incisionLevelv;
	}

	public Date getOperationdatev() {
		return operationdatev;
	}

	public void setOperationdatev(Date operationdatev) {
		this.operationdatev = operationdatev;
	}

	public String getAnaesthesiav() {
		return anaesthesiav;
	}

	public void setAnaesthesiav(String anaesthesiav) {
		this.anaesthesiav = anaesthesiav;
	}

	public String getAnaesthesiaDv() {
		return anaesthesiaDv;
	}

	public void setAnaesthesiaDv(String anaesthesiaDv) {
		this.anaesthesiaDv = anaesthesiaDv;
	}

	public String getOpsDv() {
		return opsDv;
	}

	public void setOpsDv(String opsDv) {
		this.opsDv = opsDv;
	}

	public String getOpsFirstAidv() {
		return opsFirstAidv;
	}

	public void setOpsFirstAidv(String opsFirstAidv) {
		this.opsFirstAidv = opsFirstAidv;
	}

	public String getOpsSecondAidv() {
		return opsSecondAidv;
	}

	public void setOpsSecondAidv(String opsSecondAidv) {
		this.opsSecondAidv = opsSecondAidv;
	}

	public String getOpsNv() {
		return opsNv;
	}

	public void setOpsNv(String opsNv) {
		this.opsNv = opsNv;
	}

	public String getAnaesthesiad() {
		return anaesthesiad;
	}

	public void setAnaesthesiad(String anaesthesiad) {
		this.anaesthesiad = anaesthesiad;
	}

	public String getOpsd() {
		return opsd;
	}

	public void setOpsd(String opsd) {
		this.opsd = opsd;
	}

	public String getAnaesthesia() {
		return anaesthesia;
	}

	public void setAnaesthesia(String anaesthesia) {
		this.anaesthesia = anaesthesia;
	}

	public String getOpsFirstAid() {
		return opsFirstAid;
	}

	public void setOpsFirstAid(String opsFirstAid) {
		this.opsFirstAid = opsFirstAid;
	}

	public String getOpsSecondAid() {
		return opsSecondAid;
	}

	public void setOpsSecondAid(String opsSecondAid) {
		this.opsSecondAid = opsSecondAid;
	}

	public String getOpsn() {
		return opsn;
	}

	public void setOpsn(String opsn) {
		this.opsn = opsn;
	}

	@Transient
	public String getPatientid()
	{
		return patientid;
	}

	public void setPatientid(String patientid)
	{
		this.patientid = patientid;
	}

}
