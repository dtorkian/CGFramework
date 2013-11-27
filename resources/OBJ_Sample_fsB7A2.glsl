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

in  vec3 vNormal;
in  vec3 diffuseReflection;
out vec4 FragColor;



void main(void)
{


	FragColor = vec4( diffuseReflection, 1.0 );


}