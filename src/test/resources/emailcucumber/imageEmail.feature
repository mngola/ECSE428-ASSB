Feature: Email an Image
  Send an email through Gmail with an image attached

  Scenario Outline: Sending an email after attaching an image file
    Given I am in the New Message window
    And I have entered a valid email: "<recipient>"
    When I select a valid image "<filepath>"
    Then the image should be attached to the email
    And the image should be included after sending the email

    Examples:
    |        recipient            |         filepath                                                                              |
    |   curejum@proeasyweb.com    |   D://Documents/IDEA Projects/ecse428-AssB/emailcucumber/attachments/Sketch.png               |
    |   q411605@nwytg.net         |   D://Documents/IDEA Projects/ecse428-AssB/emailcucumber/attachments/A.txt                    |
    |   pobexux@web-experts.net   |   D://Documents/IDEA Projects/ecse428-AssB/emailcucumber/attachments/AssignmentBDetails.pdf   |
    |   nonakoci@web-experts.net  |   D://Documents/IDEA Projects/ecse428-AssB/emailcucumber/attachments/test.txt                 |
    |   curejum@proeasyweb.com    |   D://Documents/IDEA Projects/ecse428-AssB/emailcucumber/attachments/test.mp3                 |

  Scenario Outline: Sending an email after attaching a photo
    Given I am in the New Message window
    And I have entered a valid email: "<recipient>"
    When I click the 'Insert photo' button
    And I switch to 'attach' mode
    And select a photo "<photoname>"
    Then the image should be attached to the email
    And the image should be included after sending the email

    Examples:
      |            recipient                |               photoname                |
      |      pobexux@web-experts.net        |     IMG_20181203_115532.jpg            |

  Scenario Outline: Sending an email after uploading an image larger than 25mb
    Given I am in the New Message window
    And I have entered a valid email: "<recipient>"
    When I select an image "<file>" larger than 25mb
    Then a large file warning should be displayed
    And a link to file should be included in the email

    Examples:
      |               recipient              |                     file                    |
      |       pobexux@web-experts.net        |     D://Documents/IDEA Projects/ecse428-AssB/emailcucumber/attachments/audacity-win-2.1.2.exe    |
