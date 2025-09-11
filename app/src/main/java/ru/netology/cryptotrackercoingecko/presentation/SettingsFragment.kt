package ru.netology.cryptotrackercoingecko.presentation

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.netology.cryptotrackercoingecko.R
import ru.netology.cryptotrackercoingecko.data.settings.SettingsManager
import ru.netology.cryptotrackercoingecko.databinding.FragmentSettingsBinding
import java.util.Locale

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() =_binding ?: throw RuntimeException("FragmentSettingsBinding is null")
    private lateinit var settingsManager: SettingsManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingsManager = SettingsManager(requireContext())

        setupCurrentSettings()
        setupSaveButton()
    }

    private fun setupCurrentSettings() {
        when (settingsManager.currentTheme) {
            SettingsManager.THEME_LIGHT -> binding.themeRadioGroup.check(R.id.lightTheme)
            SettingsManager.THEME_DARK -> binding.themeRadioGroup.check(R.id.darkTheme)
        }

        when (settingsManager.currentLanguage) {
            SettingsManager.LANGUAGE_EN -> {
                binding.languageRadioGroup.check(R.id.englishLanguage)
                if (Locale.getDefault().language != SettingsManager.LANGUAGE_EN) {
                    updateLocale(SettingsManager.LANGUAGE_EN)
                }
            }
            SettingsManager.LANGUAGE_RU -> binding.languageRadioGroup.check(R.id.russianLanguage)
        }
    }

    private fun setupSaveButton() {
        binding.saveButton.setOnClickListener {

            when (binding.themeRadioGroup.checkedRadioButtonId) {
                R.id.lightTheme -> settingsManager.currentTheme = SettingsManager.THEME_LIGHT
                R.id.darkTheme -> settingsManager.currentTheme = SettingsManager.THEME_DARK
            }

            val newLanguage = when (binding.languageRadioGroup.checkedRadioButtonId) {
                R.id.englishLanguage -> SettingsManager.LANGUAGE_EN
                R.id.russianLanguage -> SettingsManager.LANGUAGE_RU
                else -> SettingsManager.LANGUAGE_EN
            }
            settingsManager.currentLanguage = newLanguage
            updateLocale(newLanguage)

            findNavController().popBackStack()
        }
    }

    private fun updateLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        requireActivity().apply {
            createConfigurationContext(config)
            resources.updateConfiguration(config, resources.displayMetrics)
            recreate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}