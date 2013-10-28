package wordcram.text;

import wordcram.MyPApplet;

/*
 Copyright 2010 Daniel Bernier

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

public class WebPage implements TextSource {

	private String url;
	private transient MyPApplet parent;

	public WebPage(String url, MyPApplet parent) {
		this.url = url;
		this.parent = parent;
	}

	public String getText() {
		String html = MyPApplet.join(parent.loadStrings(url), ' ');
		return new Html2Text().text(html);
	}

}
