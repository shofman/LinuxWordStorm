package wordcram;

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
/*
 * Modified by Scott Hofman 2013
 * Changes Made - Made Serializable, Replaced any Processing functionality with my own applets that mirror
 * the processing functionality, but are serializable. 
 */
import java.io.Serializable;

/**
 * A WordFonter tells WordCram what font to render a word in.
 * <p>
 * Some useful implementations are available in {@link Fonters}.
 * <p>
 * <i>The name "WordFonter" was picked because it matches the others: WordColorer,
 * WordPlacer, WordSizer, etc. Apologies if it sounds a bit weird to your ear; I
 * eventually got used to it.</i>
 * 
 * @author Dan Bernier
 */
public interface WordFonter extends Serializable {
	
	/**
	 * What font should this {@link Word} be drawn in?
	 * @param word the word to pick the MyPFont for
	 * @return the MyPFont for the word
	 */
	public MyPFont fontFor(Word word);
}
