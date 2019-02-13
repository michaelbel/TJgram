package org.michaelbel.tjgram.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.michaelbel.tjgram.R
import org.michaelbel.tjgram.data.UserConfig
import org.michaelbel.tjgram.ui.profile.ProfileFragment

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        if (preference?.key == "key_logout") {
            return logout()
        }

        return super.onPreferenceTreeClick(preference)
    }

    private fun logout(): Boolean {
        if (UserConfig.isAuthorized(requireContext()).not())
            return true

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.app_name)
            .setMessage(R.string.logout_dialog_message)
            .setPositiveButton(R.string.action_ok) {_,_ ->
                UserConfig.userLogout(requireContext())
                val intent = Intent()
                intent.putExtra(ProfileFragment.LOGOUT_RESULT, true)
                (activity as SettingsActivity).setResult(Activity.RESULT_OK, intent)
                (activity as SettingsActivity).finish()
            }
            .setNegativeButton(R.string.action_cancel, null)
            .show()

        return true
    }
}