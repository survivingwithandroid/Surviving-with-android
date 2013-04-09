/*
 * Copyright (C) 2013 jfrankie (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.survivingwithandroid.listviewroundcorners;

public class Planet {
	
	private String name;
	private Integer distance;
	private String descr;
	private int idImg;
	
	
	
	public Planet(String name, Integer distance) {
		this.name = name;
		this.distance = distance;
	}
	
	public Planet(String name, String descr) {
		this.name = name;
		this.descr = descr;
	}

	public Planet(String name, Integer distance, String descr, int idImg) {
		this.name = name;
		this.distance = distance;
		this.descr = descr;
		this.idImg = idImg;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getDistance() {
		return distance;
	}
	public void setDistance(Integer distance) {
		this.distance = distance;
	}
	public int getIdImg() {
		return idImg;
	}
	public void setIdImg(int idImg) {
		this.idImg = idImg;
	}
	
	
}
