/* 
 * Cologne University of Applied Sciences
 * Institute for Media and Imaging Technologies - Computer Graphics Group
 *
 * Copyright (c) 2012 Cologne University of Applied Sciences. All rights reserved.
 *
 * This source code is property of the Cologne University of Applied Sciences. Any redistribution
 * and use in source and binary forms, with or without modification, requires explicit permission. 
 */


#version 150

out     vec4 FragColor;
uniform vec3 uColor;
in vec3 vPosition;
in float time;

void main(void)
{

if (vPosition.y>0.35){

	FragColor = vec4( 0.0,0.0,0.0, 1.0 );
	
	}
else if (vPosition.y>-.35){

	FragColor = vec4(1.0, 0.0, 0.0 , 1.0 );
	
	}
	else{
	
	FragColor = vec4(1.0, 1.0, 0.0 , 1.0 );
	
	
	}
	

}