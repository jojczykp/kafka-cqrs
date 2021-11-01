import React, { Component } from "react";
import ReactDOM from "react-dom";
import InputButton from "../presentational/InputButton.jsx";
import OutputNotifications from "../presentational/OutputNotifications.jsx";
import OutputConnectionStatus from "../presentational/OutputConnectionStatus.jsx";

class NotificationsContainer extends Component {

  constructor() {
    super();

    this.state = {
      notifications: [],
      readyState: 0
    };

    this.handleNotification = this.handleNotification.bind(this);
    this.handleClickPause = this.handleClickPause.bind(this);
    this.handleClickResume = this.handleClickResume.bind(this);
    this.handleClickClear = this.handleClickClear.bind(this);
    this.handleOpen = this.handleOpen.bind(this);
    this.handleError = this.handleError.bind(this);

    this.createEventSource();
  }

  createEventSource() {
    this.source = new EventSource(window.location.origin + '/notifier/documents/');
    this.source.onmessage = this.handleNotification;
    this.source.onopen = this.handleOpen;
    this.source.onerror = this.handleError;
  }

  handleNotification(event) {
    this.state.notifications.push(event.data);
    this.setState(prevState => prevState);

    var textArea = document.getElementsByClassName('output-notifications-control')[0];
    textArea.scrollTop = textArea.scrollHeight;
  }

  handleClickPause(event) {
    this.source && this.source.close();
    this.source = null;

    this.setState(prevState => ({
      ...prevState,
      readyState: null
    }));
  }

  handleClickResume(event) {
    this.source || this.createEventSource();
    this.setState(prevState => ({
      ...prevState,
      readyState: this.source.readyState
    }));
  }

  handleClickClear(event) {
    this.setState(prevState => ({
      ...prevState,
      notifications: []
    }));
  }

  handleOpen(event) {
    this.setState(prevState => ({
      ...prevState,
      readyState: event.target.readyState
    }));
  }

  handleError(event) {
    var isConnectionExpected = (this.state.readyState != null);
    var isDisconnectError = (event.target.readyState == EventSource.CLOSED);

    this.setState(prevState => ({
      ...prevState,
      readyState: event.target.readyState
    }));

    if (isConnectionExpected && isDisconnectError) {
      setTimeout(() => {
        var isConnectionExpected = (this.state.readyState != null);
        if (isConnectionExpected) {
          this.createEventSource();
        }
      }, 2000);
    }
  }

  isPaused() {
    return (this.state.readyState == null);
  }

  render() {
    const { notifications, readyState } = this.state;
    return (
      <div className="view-container">
        <div>
          <div className="align-left">
            <OutputConnectionStatus
              id="notifications-connection-status"
              value={readyState}
            />
            <div className="view-title">SSE Push Notifications</div>
          </div>

          <div className="align-right">
            <InputButton
              id="notifications-clear-button"
              value="Clear"
              handleClick={this.handleClickClear}
            />
            {this.isPaused() &&
            <InputButton
              id="notifications-resume-button"
              value="&#x25B6;"
              handleClick={this.handleClickResume}
            />}
            {!this.isPaused() &&
            <InputButton
              id="notifications-pause-button"
              value="&#x2503;&#x2503;"
              handleClick={this.handleClickPause}
            />}
          </div>
        </div>
        <OutputNotifications
          id="notifications-id"
          value={notifications}
        />
      </div>
    );
  }
}

export default NotificationsContainer;

const wrapper = document.getElementById("notifications-div");
wrapper ? ReactDOM.render(<NotificationsContainer />, wrapper) : false;
