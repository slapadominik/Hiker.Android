package com.hiker.presentation.user.tripParticipant


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.hiker.R
import com.hiker.domain.entities.Status
import com.hiker.presentation.login.LoginViewModelFactory
import com.hiker.presentation.user.UserViewModel
import kotlinx.android.synthetic.main.fragment_trip_participant_view.*
import java.lang.Exception
import java.util.*


class TripParticipantView : Fragment() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_participant_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        arguments?.let {
            val safeArgs = TripParticipantViewArgs.fromBundle(it)
            setupObservers(safeArgs.userId)
        }
        trip_participant_toolbar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }
    }

    private fun setupObservers(userSystemId: String){
        userViewModel.getUser(UUID.fromString(userSystemId)).observe(requireActivity(), Observer{
            if (it.status == Status.SUCCESS){
                val user = it.data
                if (user!= null){
                    tripParticipant_firstName_textView.text = user.firstName
                    tripParticipant_lastName_textView.text = user.lastName
                    if (user.birthday != null){
                        tripParticipant_age_textView.text =  com.hiker.domain.extensions.Period.between(user.birthday, Calendar.getInstance().time).toString()
                    }
                    tripParticipant_imageView.visibility = View.VISIBLE
                    imgProgress.visibility = View.GONE
                    userViewModel.setUserThumbnail(tripParticipant_imageView, user.facebookId)

                    if (!user.aboutMe.isNullOrEmpty()){
                        tripParticipant_aboutMe_textView.text = user.aboutMe
                    }
                    if (!user.phoneNumber.isNullOrEmpty()){
                        tripParticipant_phoneNumber_textView.text = PhoneNumberUtils.formatNumber(user.phoneNumber, "PL")
                        tripParticipant_phoneNumber_textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.com_facebook_blue))
                        tripParticipant_phoneNumber_textView.setOnClickListener {
                            val intent = Intent(Intent.ACTION_DIAL);
                            intent.data = Uri.parse("tel:"+user.phoneNumber);
                            startActivity(intent)
                        }
                    }
                }
            }
            else{
                Snackbar.make(requireView(), it.message!!, Snackbar.LENGTH_LONG).show()
            }
        })
    }


    private fun initViewModel() {
        userViewModel = ViewModelProviders.of(this, LoginViewModelFactory(requireContext())).get(UserViewModel::class.java)
    }
}
