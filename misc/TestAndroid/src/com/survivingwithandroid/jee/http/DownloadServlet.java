/*
 * Copyright (C) 2014 Francesco Azzola
 *  Surviving with Android (http://www.survivingwithandroid.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.survivingwithandroid.jee.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class DownloadServlet
 */
@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		downloadData(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		downloadData(request, response);
	}
	
	private void downloadData(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String realPath = request.getServletContext().getRealPath("/images");
		String imgName = request.getParameter("name");
		String imgPath = realPath + File.separator + imgName;
		File f = new File(imgPath);
		FileInputStream fis = null;
		
		byte[] buffer = new byte[1024];
		
		response.setContentLength((int) f.length());
		
		try {
			fis = new FileInputStream(f);
			OutputStream os = response.getOutputStream();
			while ( fis.read(buffer) != -1) 
				os.write(buffer);
		}
		catch(Throwable t) {
			// Handle exception here;
			t.printStackTrace();
		}
		finally {
			try {fis.close(); } catch(Throwable t) {}
		}
		
	}

}
