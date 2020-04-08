package com.hiker.presentation.user.edit

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hiker.R
import com.hiker.domain.entities.User
import com.hiker.presentation.login.LoginViewModelFactory
import com.hiker.presentation.user.UserViewModel
import kotlinx.android.synthetic.main.fragment_user_edit_view.*
import java.text.SimpleDateFormat
import java.util.*

class UserEditView : Fragment() {

    private lateinit var viewModel: UserEditViewModel
    private lateinit var bottomNavigationView: BottomNavigationView
    private val dateFormater = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY)
    private lateinit var birthday : Date
    private val birthdayCalendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_edit_view, container, false)
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigation)
        bottomNavigationView.visibility = View.GONE
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val userSystemId = sharedPref.getString(getString(R.string.preferences_userSystemId), null)
        viewModel.getUsersData(UUID.fromString(userSystemId)).observe(requireActivity(), Observer {
            if (it != null){
                birthday = it.birthday
                user_edit_firstNameInput.setText(it.firstName)
                user_edit_lastNameInput.setText(it.lastName)
                user_edit_birthdayInput.setText(dateFormater.format(it.birthday))
                user_edit_phoneNumberInput.setText(it.phoneNumber)
                user_edit_aboutMeInput.setText(it.aboutMe)
            }
        })
        user_edit_submitButton.setOnClickListener{
            val firstName = user_edit_firstNameInput.text.toString()
            val lastName = user_edit_lastNameInput.text.toString()

            val phoneNumber = user_edit_phoneNumberInput.text.toString()
            val aboutMe = user_edit_aboutMeInput.text.toString()
            val user = User(
                id = userSystemId,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
                birthday = birthday,
                aboutMe = aboutMe,
                facebookId = ""
            )
            viewModel.editUser(user).observe(requireActivity(), Observer {
                findNavController().popBackStack()
            })
        }

        user_edit_birthday.setOnFocusChangeListener{x, hasFocus ->
            if (hasFocus){
                DatePickerDialog(requireContext(), {view, year, month, day ->
                    birthdayCalendar.set(year, month, day)
                    user_edit_birthdayInput.setText(dateFormater.format(birthdayCalendar.time))
                    birthday = birthdayCalendar.time
                }, birthdayCalendar.get(Calendar.YEAR), birthdayCalendar.get(Calendar.MONTH), birthdayCalendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, UserEditViewModelFactory(requireContext())).get(
            UserEditViewModel::class.java)
    }
}