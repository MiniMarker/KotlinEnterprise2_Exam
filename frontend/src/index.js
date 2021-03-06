import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import {BrowserRouter, Route} from "react-router-dom";
import Welcome from "./components/Welcome";
import NowPlayings from "./components/NowPlayings";
import SignUp from "./components/SignUp";
import Room from "./components/Room";
import SignIn from "./components/SignIn";
import Invoice from "./components/Invoice";
import LogOut from "./components/LogOut";
import Done from "./components/Done";

ReactDOM.render(
	<BrowserRouter>
		<App>
			<Route path="/" exact component={Welcome} />
			<Route path="/nowPlayings" component={NowPlayings} />
			<Route path="/signup" component={SignUp} />
			<Route path="/signin" component={SignIn} />
			<Route path="/logout" component={LogOut} />
			<Route path="/done" component={Done} />
			<Route path="/booking" component={Room} />
			<Route path="/invoices/:id" component={Invoice} />
		</App>
	</BrowserRouter>
	, document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.unregister();


