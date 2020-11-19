package com.baloise.sharedlib.tools.internal

import java.nio.file.*

import com.baloise.sharedlib.common.Registered
import com.baloise.sharedlib.tools.Maven

class MavenImpl extends Registered implements Maven{

	@Override
	// tag see https://hub.docker.com/_/maven
	public void mvn(String cmd, String tag = "") {
		String image = tag ? "maven:$tag" : "maven"
		if(steps.isUnix()) {
			steps.docker.image(image).inside('-v $HOME/.m2:/root/.m2') {
                steps.sh "mvn $cmd"
			}
		} else {
			String sname = "docker-settings.xml"
			String settings = 
				Files.exists(Paths.get(System.getProperty("user.home"), '.m2').toRealPath().resolve(sname)) ? 
				"-s /root/.m2/$sname" : 
				""
			steps.bat "docker run --rm -v \"%cd%\":/usr/src/mymaven -v \"%userprofile%\\.m2\":/root/.m2 -w /usr/src/mymaven $image mvn $settings $cmd"
		}
	}
}
