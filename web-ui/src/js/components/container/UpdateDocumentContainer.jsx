import React, { Component } from "react";
import ReactDOM from "react-dom";
import InputId from "../presentational/InputId.jsx";
import InputAuthor from "../presentational/InputAuthor.jsx";
import InputTitle from "../presentational/InputTitle.jsx";
import InputButton from "../presentational/InputButton.jsx";

class UpdateDocumentContainer extends Component {
  constructor() {
    super();
    this.state = {
      seo_title: ""
    };
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(event) {
    this.setState({ [event.target.id]: event.target.value });
  }

  render() {
    const { seo_title } = this.state;
    return (
      <form id="update-document-form">
        <InputId
          id="update-document-id"
          label="Id:"
          value={seo_title}
          handleChange={this.handleChange}
        />
        <InputAuthor
          id="update-document-author"
          label="Author:"
          value={seo_title}
          handleChange={this.handleChange}
        />
        <InputTitle
          id="update-document-title"
          label="Title:"
          value={seo_title}
          handleChange={this.handleChange}
        />
        <InputButton
          id="update-document-button"
          value="Update Document"
          handleChange={this.handleChange}
        />
      </form>
    );
  }
}

export default UpdateDocumentContainer;

const wrapper = document.getElementById("update-document-div");
wrapper ? ReactDOM.render(<UpdateDocumentContainer />, wrapper) : false;
