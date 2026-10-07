package com.conecel.claro.bnas.m2m.generate_document.bean.generate;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonHeaderRequest implements Serializable{


	private ChannelInfo channelInfo;
	private ConsumerInfo consumerInfo;
	private GeolocationInfo geolocationInfo;

}
