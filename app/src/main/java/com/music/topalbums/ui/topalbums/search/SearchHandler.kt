package com.music.topalbums.ui.topalbums.search

import androidx.core.widget.doOnTextChanged
import com.music.topalbums.databinding.ViewTextSearchBinding

import com.music.topalbums.logger.Logger.loggable

/***
 * A GUI control that receives the text as being typed and informed about relevant changes.
 *  - if the search string has 3 or more characters an event with the search text will be fired.
 *  - if the search string has less than 3 chars an event with null will be fired. This null indicates
 *  that search string is too short to be useful for filtering.
 */
class SearchHandler(val binding: ViewTextSearchBinding, onSearchChanged: (String?) -> Unit)
{
    val TAG = SearchHandler::class.java.simpleName
    companion object const val MIN_LENGTH = 3

    //the last text being fired (used for decision if a new event should be fired when the entered text is too short)
    private var oldTextFired: String? = null
    init
    {
        binding.searchEditText.doOnTextChanged { text, start, before, count ->
            val enteredText = binding.searchEditText.text.toString()

            if (enteredText.length >= MIN_LENGTH ) //long enough
            {
                onSearchChanged(enteredText)
                oldTextFired = enteredText
            }
            else //too short
            {
                if(oldTextFired != null)
                {
                    //the old text was long enough, so inform that the new text is too short
                    onSearchChanged(null)
                    oldTextFired = null
                }
                else
                {
                    //the old text was also too short, so no need to inform that the new text is short too
                    loggable.d(TAG, "Nothing to fire, the entered text = " + enteredText)
                }
            }
        }
    }

}