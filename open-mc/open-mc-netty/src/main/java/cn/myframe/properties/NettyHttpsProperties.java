package cn.myframe.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import io.netty.util.internal.SystemPropertyUtil;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@ConfigurationProperties(prefix="netty.https")
@Data
@Validated
public class NettyHttpsProperties {

	@NotNull(message = "端口不能为空")
	@Range(min=1000, max=60000)
	private Integer port;
	
	@NotNull(message = "https的证书不能为空")
	private String certFile;
	
	@NotNull(message = "https的证书密钥不能为空")
	private String keyFile;
	
	@Pattern(regexp="((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))",message="ip地址格式不正确")
	private String bindIp;
	
	@DecimalMin("1") ////必须大于1
	private Integer bossThreads = Math.max(1, SystemPropertyUtil.getInt(
            "io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2));
	
	@DecimalMin("1") //必须大于1
    private Integer workThreads = Math.max(1, SystemPropertyUtil.getInt(
            "io.netty.eventLoopThreads", Runtime.getRuntime().availableProcessors() * 2));

}
