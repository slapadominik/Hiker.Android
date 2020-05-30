package com.hiker.presentation.user.tripParticipant


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.hiker.R
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
        trip_participant_facebookBtn.setOnClickListener{
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/2658397584210487"));
                startActivity(intent)
            } catch(e: Exception) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/appetizerandroid")));
            }
        }
        trip_participant_toolbar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }
    }

    private fun setupObservers(userSystemId: String){
        userViewModel.getUser(UUID.fromString(userSystemId)).observe(requireActivity(), Observer{
            if (it != null){
                tripParticipant_firstName_textView.text = it.firstName
                tripParticipant_lastName_textView.text = it.lastName
                if (it.birthday != null){
                    tripParticipant_age_textView.text =  com.hiker.domain.extensions.Period.between(it.birthday, Calendar.getInstance().time).toString()
                }
                tripParticipant_imageView.visibility = View.VISIBLE
                imgProgress.visibility = View.GONE
                userViewModel.setUserThumbnail(tripParticipant_imageView, it.facebookId)

                if (!it.aboutMe.isNullOrEmpty()){
                    tripParticipant_aboutMe_textView.text = it.aboutMe
                }
                if (!it.phoneNumber.isNullOrEmpty()){
                    tripParticipant_phoneNumber_textView.text = it.phoneNumber
                }

            }
        })
    }


    private fun initViewModel() {
        userViewModel = ViewModelProviders.of(this, LoginViewModelFactory(requireContext())).get(UserViewModel::class.java)
    }
}
