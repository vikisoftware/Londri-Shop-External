package com.vikisoft.londrishops.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vikisoft.londrishops.R;
import com.vikisoft.londrishops.helper.LoginDataResponce;
import com.vikisoft.londrishops.utils.SessionManager;

import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout primeLaundryRl, addOfferRl, ticketIssueRl, priorityListingRl, bannerAdTv, aboutUsRl,
            contactUsRl, faqRl, privacyPolicyRl, termsNconditionRl, shareOptionRl;
    private ImageView backIv;
    private LoginDataResponce data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }

    private void init() {
        primeLaundryRl = findViewById(R.id.prime_subscription_rl);
        addOfferRl = findViewById(R.id.add_offer_rl);
        ticketIssueRl = findViewById(R.id.ticket_issue_rl);
        priorityListingRl = findViewById(R.id.priority_rl);
        bannerAdTv = findViewById(R.id.banner_ad_rl);
        aboutUsRl = findViewById(R.id.about_us_rl);
        contactUsRl = findViewById(R.id.contact_rl);
        faqRl = findViewById(R.id.faq_rl);
        privacyPolicyRl = findViewById(R.id.privacy_policy_rl);
        termsNconditionRl = findViewById(R.id.terms_and_condition_rl);
        backIv = findViewById(R.id.back);
        shareOptionRl = findViewById(R.id.sharing_option_rl);

        primeLaundryRl.setOnClickListener(this::onClick);
        privacyPolicyRl.setOnClickListener(this);
        addOfferRl.setOnClickListener(this);
        ticketIssueRl.setOnClickListener(this);
        priorityListingRl.setOnClickListener(this);
        bannerAdTv.setOnClickListener(this);
        aboutUsRl.setOnClickListener(this);
        contactUsRl.setOnClickListener(this);
        faqRl.setOnClickListener(this);
        privacyPolicyRl.setOnClickListener(this);
        termsNconditionRl.setOnClickListener(this);
        backIv.setOnClickListener(this);
        shareOptionRl.setOnClickListener(this::onClick);


        data = new SessionManager(SettingsActivity.this).getProfileJson();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prime_subscription_rl:
            case R.id.add_offer_rl:
            case R.id.ticket_issue_rl:
            case R.id.priority_rl:
            case R.id.banner_ad_rl:
            case R.id.faq_rl:
            case R.id.sharing_option_rl:
                Toast.makeText(this, getString(R.string.feature_wll_be_added_soon), Toast.LENGTH_SHORT).show();
                break;
            case R.id.about_us_rl:
                String url = "https://www.londri.io/about-us.jsp";
                Intent ix = new Intent(Intent.ACTION_VIEW);
                ix.setData(Uri.parse(url));
                startActivity(ix);
                break;
            case R.id.contact_rl:
                String url1 = "https://londri.io/contact-us.jsp";
                Intent ix1 = new Intent(Intent.ACTION_VIEW);
                ix1.setData(Uri.parse(url1));
                startActivity(ix1);
                break;
            case R.id.privacy_policy_rl:
                showAlert("Londri Privacy & Policy", privacyString);
                break;
            case R.id.terms_and_condition_rl:
                showAlert("Londri Terms & Conditions", tncString);
                break;
            case R.id.back_iv:
                finish();
                break;

        }
    }


    private void showAlert(String title, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());
        builder.show();

    }


    private final String tncString = "The terms \"We\" / \"Us\" / \"Our\"/ \"Company\" individually and collectively refer to ViKi Software (OPC) Pvt. Ltd. and the terms \"Visitor\" / \"User\" refer to the users.\n" +
            "\n" +
            "\n" +
            "\n" +
            "This page states the Terms and Conditions under which you (Visitor) may visit this website (\"Londri.io\"). Please read this page carefully. If you do not accept the Terms and Conditions stated here, we would request you to exit this site. The business, any of its business divisions and / or its subsidiaries, associate companies or subsidiaries to subsidiaries or such other investment companies (in India or abroad) reserve their respective rights to revise these Terms and Conditions at any time by updating this posting. You should visit this page periodically to re-appraise yourself of the Terms and Conditions, because they are binding on all users of this Website.\n" +
            "\n" +
            "\n" +
            "\n" +
            "USE OF CONTENT\n" +
            "\n" +
            "All logos, brands, marks headings, labels, names, signatures, numerals, shapes or any combinations thereof, appearing in this site, except as otherwise noted, are properties either owned, or used under license, by the business and / or its associate entities who feature on this Website. The use of these properties or any other content on this site, except as provided in these terms and conditions or in the site content, is strictly prohibited. \n" +
            "You may not sell or modify the content of this Website or reproduce, display, publicly perform, distribute, or otherwise use the materials in any way for any public or commercial purpose without the respective organization's or entity's written permission.\n" +
            "\n" +
            "\n" +
            "\n" +
            "ACCEPTABLE WEBSITE USE\n" +
            "\n" +
            "(A) Security Rules\n" +
            "Visitors are prohibited from violating or attempting to violate the security of the Web site, including, without limitation, (1) accessing data not intended for such user or logging into a server or account which the user is not authorized to access, (2) attempting to probe, scan or test the vulnerability of a system or network or to breach security or authentication measures without proper authorization, (3) attempting to interfere with service to any user, host or network, including, without limitation, via means of submitting a virus or \"Trojan horse\" to the Website, overloading, \"flooding\", \"mail bombing\" or \"crashing\", or (4) sending unsolicited electronic mail, including promotions and/or advertising of products or services. Violations of system or network security may result in civil or criminal liability. The business and / or its associate entities will have the right to investigate occurrences that they suspect as involving such violations and will have the right to involve, and cooperate with, law enforcement authorities in prosecuting users who are involved in such violations.\n" +
            "\n" +
            "\n" +
            "(B) General Rules\n" +
            "Visitors may not use the Web Site in order to transmit, distribute, store or destroy material (a) that could constitute or encourage conduct that would be considered a criminal offence or violate any applicable law or regulation, (b) in a manner that will infringe the copyright, trademark, trade secret or other intellectual property rights of others or violate the privacy or publicity of other personal rights of others, or (c) that is libelous, defamatory, pornographic, profane, obscene, threatening, abusive or hateful.\n" +
            "\n" +
            "\n" +
            "\n" +
            "INDEMNITY\n" +
            "\n" +
            "The User unilaterally agree to indemnify and hold harmless, without objection, the Company, its officers, directors, employees and agents from and against any claims, actions and/or demands and/or liabilities and/or losses and/or damages whatsoever arising from or resulting from their use of www.Londri.io or their breach of the terms .\n" +
            "\n" +
            "\n" +
            "\n" +
            "LIABILITY\n" +
            "\n" +
            "User agrees that neither Company nor its group companies, directors, officers or employee shall be liable for any direct or/and indirect or/and incidental or/and special or/and consequential or/and exemplary damages, resulting from the use or/and the inability to use the service or/and for cost of procurement of substitute goods or/and services or resulting from any goods or/and data or/and information or/and services purchased or/and obtained or/and messages received or/and transactions entered into through or/and from the service or/and resulting from unauthorized access to or/and alteration of user's transmissions or/and data or/and arising from any other matter relating to the service, including but not limited to, damages for loss of profits or/and use or/and data or other intangible, even if Company has been advised of the possibility of such damages. User further agrees that Company shall not be liable for any damages arising from interruption, suspension or termination of service, including but not limited to direct or/and indirect or/and incidental or/and special consequential or/and exemplary damages, whether such interruption or/and suspension or/and termination was justified or not, negligent or intentional, inadvertent or advertent. User agrees that Company shall not be responsible or liable to user, or anyone, for the statements or conduct of any third party of the service. In sum, in no event shall Company's total liability to the User for all damages or/and losses or/and causes of action exceed the amount paid by the User to Company, if any, that is related to the cause of action.\n" +
            "\n" +
            "\n" +
            "\n" +
            "For End User\n" +
            "\n" +
            "Torn Clothes:\n" +
            "We don't accept or process torn clothes, including fashioned clothes, if for any reason, on the request of our beloved customer and on their sole responsibility our Londri Runner may pick up fashioned clothes.\n" +
            "\n" +
            "\n" +
            "Payment Problems \n" +
            "For any circumstances such as and not limited to pending payment Londri will reflect payment to your associated bank account within 3 to 5 working days.\n" +
            "\n" +
            "\n" +
            "Clothes Damage or mismatch:\n" +
            "To avoid such hurdle situation, try to check your clothes before and after each pickup and delivery. Ask Londri Runner to take photos and add tag to your clothes. o In any situation of damaging clothes or clothes mismatch Londri will not take any responsibility or assure you any kind of compensation. You can contact Londri support if these kinds of situation occurred. Any mislead or misguiding attitude resulting permanent banning of your Londri Account. o Under such situation of mismatch or damage of clothes the Laundry Store would might compassionate. Londri will Connect you with associated vendor representatives.\n" +
            "\n" +
            "\n" +
            "Refer and Earn program:\n" +
            "Londri App runs refer and earn program to spread awareness of brand. To function this program as intended we expecting your android id for unique verification of android device. Also, we use location access to deliver better services to our beloved users. o If you refer someone successfully you can avail free pickup and delivery for your next Londri order. o If our system detected any unusual behavior your account might get suspended for some times.\n" +
            "\n" +
            "\n" +
            "\n" +
            "For Laundry Shops & Owners\n" +
            "\n" +
            "Londri.io established and intended to provide clothes pickup and delivery facility, to give pace to the laundry businesses, by being catalyst between Laundry professionals and our customers we ensure to treat them and their clothes specially. If you believe in offering same value and services connect with us.\n" +
            "\n" +
            "\n" +
            "Clothes Damage or Mismatch:\n" +
            "To avoid such hurdle situation, we check our customers clothes before and after each pickup and delivery. Our Londri Runner takes photos and add tag to customer clothes to avoid further hassle.\n" +
            "\n" +
            "\n" +
            "As we acting as only pickup-delivery service Londri or its parent company will not take any responsibility to compensate customers clothes damage. After investigation if we found out the mistake from your side & on eager customers request Laundry owner has to companionate customer the amount agreed by Londri representative, Laundry Owner and Customer.\n" +
            "\n" +
            "\n" +
            "For more detailed information contact us. \n" +
            "\n" +
            "DISCLAIMER OF CONSEQUENTIAL DAMAGES\n" +
            "\n" +
            "In no event shall Company or any parties, organizations or entities associated with the corporate brand name us or otherwise, mentioned at this Website be liable for any damages whatsoever (including, without limitations, incidental and consequential damages, lost profits, or damage to computer hardware or loss of data information or business interruption) resulting from the use or inability to use the Website and the Website material, whether based on warranty, contract, tort, or any other legal theory, and whether or not, such organization or entities were advised of the possibility of such damages. In no event shall Company or any parties, organizations or entities associated with the corporate brand name us or otherwise, mentioned at this Website be liable for any damages whatsoever including clothes and other accessories processed by third party vendors.\n" +
            "\n" +
            "\n" +
            "\n" +
            "REFUND & CANCELLATION POLICY\n" +
            "\n" +
            "Our focus is complete customer satisfaction. In the event, if you are displeased with the services provided, we will refund back the money, provided the reasons are genuine and proved after investigation. Please check your clothes before pickup and delivery, contact us immediate if any unusual happens.\n" +
            "\n" +
            "In case of dissatisfaction from our services, clients have the liberty to cancel their order and request a refund from us. Our Policy for the cancellation and refund will be as follows:\n" +
            "\n" +
            "\n" +
            "\n" +
            "Cancellation Policy\n" +
            "Once our agent picked order from your address, the cancellation nearly possible before it gets reached at our partnered vendor. If order gets cancelled before processing, In such circumstances, the delivery charges and taxes will not be refunded.\n" +
            "\n" +
            "To know more about Cancellations please contact us via contact us page link by on our website.\n" +
            "\n" +
            "Contact Us \n" +
            "\n" +
            "Refund Policy\n" +
            "We will try our best to find the suitable laundry services near clients.\n" +
            "\n" +
            "In case any client is not completely satisfied with our services we can provide a refund.\n" +
            "\n" +
            "If paid by credit card, refunds will be issued to the original credit card provided at the time of purchase and in case of payment gateway name payments refund will be made to the same account. Taxes and processing fees will not be including at return.\n" +
            "\n" +
            "\n" +
            "Payment policy\n" +
            "After each successful delivery Laundry owner or laundry shop associated bank account will get credited by order amount excluding the charges of Londri services.\n" +
            "\n" +
            "\n" +
            "Use Case:\n" +
            "Item\tCharge\tAmount\n" +
            "Total clothes, Order amount:\t\t07 piece 180₹ \n" +
            "Londri Charge:\t5% + GST\t09₹ \n" +
            "Total amount laundry receive:\t\t180-7₹  = 173₹ \n" +
            "\n" +
            "Note: The charges may vary from time and location, check this page periodically.";
    private final String privacyString =
            "\n" +
                    "The terms \"We\" / \"Us\" / \"Our\"/ \"Company\" individually and collectively refer to ViKi Software (OPC) Pvt. Ltd. and the terms \"Visitor\" / \"User\" refer to the users.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "This Privacy Policy is an electronic record in the form of an electronic contract formed under the information Technology Act, 2000 and the rules made thereunder and the amended provisions pertaining to electronic documents / records in various statutes as amended by the information Technology Act, 2000. This Privacy Policy does not require any physical, electronic or digital signature.\n" +
                    "\n" +
                    "\n" +
                    "This Privacy Policy is a legally binding document between you and ViKi Software (OPC) Pvt. Ltd. (both terms defined below). The terms of this Privacy Policy will be effective upon your acceptance of the same (directly or indirectly in electronic form, by clicking on the I accept tab or by use of the website or by other means) and will govern the relationship between you and ViKi Software (OPC) Pvt. Ltd. for your use of the website \"Londri.io\" (defined below).\n" +
                    "\n" +
                    "\n" +
                    "This document is published and shall be construed in accordance with the provisions of the Information Technology (reasonable security practices and procedures and sensitive personal data of information) rules, 2011 under Information Technology Act, 2000; that require publishing of the Privacy Policy for collection, use, storage and transfer of sensitive personal data or information.\n" +
                    "\n" +
                    "\n" +
                    "Please read this Privacy Policy carefully by using the Website, you indicate that you understand, agree and consent to this Privacy Policy. If you do not agree with the terms of this Privacy Policy, please do not use this Website.\n" +
                    "\n" +
                    "\n" +
                    "By providing us your Information or by making use of the facilities provided by the Website, You hereby consent to the collection, storage, processing and transfer of any or all of Your Personal Information and Non-Personal Information by us as specified under this Privacy Policy. You further agree that such collection, use, storage and transfer of Your Information shall not cause any loss or wrongful gain to you or any other person.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "USER INFORMATION\n" +
                    "\n" +
                    "To avail certain services on our Websites, users are required to provide certain information for the registration process namely: - a) your name, b) email address, c) sex, d) age, e) PIN code, f) credit card or debit card details, j) password k) Location & address details L) Your Photo M) Android ID N) Location information etc. The Information as supplied by the users enables us to improve our sites and provide you the most user-friendly experience.\n" +
                    "\n" +
                    "\n" +
                    "All required information is service dependent and we may use the above said user information to, maintain, protect, and improve its services (including advertising services) and for developing new services\n" +
                    "\n" +
                    "\n" +
                    "Such information will not be considered as sensitive if it is freely available and accessible in the public domain or is furnished under the Right to Information Act, 2005 or any other law for the time being in force.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "COOKIES\n" +
                    "\n" +
                    "To improve the responsiveness of the sites for our users, we may use \"cookies\", or similar electronic tools to collect information to assign each visitor a unique, random number as a User Identification (User ID) to understand the user's individual interests using the Identified Computer. Unless you voluntarily identify yourself (through registration, for example), we will have no way of knowing who you are, even if we assign a cookie to your computer. The only personal information a cookie can contain is information you supply (an example of this is when you ask for Your past orders). A cookie cannot read data off your hard drive.\n" +
                    "\n" +
                    "\n" +
                    "Our web servers automatically collect limited information. about your computer's connection to the Internet, including your IP address, when you visit our site. (Your IP address is a number that lets computers attached to the Internet know where to send you data -- such as the web pages you view.) Your IP address does not identify you personally. We use this information to deliver our web pages to you upon request, to tailor our site to the interests of our users, to measure traffic within our site and let advertisers know the geographic locations from where our visitors come.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "LINKS TO THE OTHER SITES\n" +
                    "\n" +
                    "Our policy discloses the privacy practices for our own web site only. Our site provides links to other websites also that are beyond our control. We shall in no way be responsible in way for your use of such sites.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "INFORMATION SHARING\n" +
                    "\n" +
                    "We shares the sensitive personal information to any third party without obtaining the prior consent of the user in the following limited circumstances:\n" +
                    "\n" +
                    "\n" +
                    "(a) When it is requested or required by law or by any court or governmental agency or authority to disclose, for the purpose of verification of identity, or for the prevention, detection, investigation including cyber incidents, or for prosecution and punishment of offences. These disclosures are made in good faith and belief that such disclosure is reasonably necessary for enforcing these Terms; for complying with the applicable laws and regulations.\n" +
                    "\n" +
                    "\n" +
                    "(b) We proposes to share such information within its group companies and officers and employees of such group companies for the purpose of processing personal information on its behalf. We also ensure that these recipients of such information agree to process such information based on our instructions and in compliance with this Privacy Policy and any other appropriate confidentiality and security measures.\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "INFORMATION SECURITY\n" +
                    "\n" +
                    "We take appropriate security measures to protect against unauthorized access to or unauthorized alteration, disclosure or destruction of data. These include internal reviews of our data collection, storage and processing practices and security measures, including appropriate encryption and physical security measures to guard against unauthorized access to systems where we store personal data.\n" +
                    "\n" +
                    "\n" +
                    "All information gathered on our Website is securely stored within our controlled database. The database is stored on servers secured behind a firewall; access to the servers is password-protected and is strictly limited. However, as effective as our security measures are, no security system is impenetrable. We cannot guarantee the security of our database, nor can we guarantee that information you supply will not be intercepted while being transmitted to us over the Internet. And, of course, any information you include in a posting to the discussion areas is available to anyone with Internet access.\n" +
                    "\n" +
                    "\n" +
                    "However the internet is an ever evolving medium. We may change our Privacy Policy from time to time to incorporate necessary future changes. Of course, our use of any information we gather will always be consistent with the policy under which the information was collected, regardless of what the new policy may be.";

}
