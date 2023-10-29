import React from "react"
import download from "./download.png"
import { FaSearch} from 'react-icons/fa';
import logo from "./gfg-gg-logo.svg"
import DSA from"./Data-Structures-With-Python-thumbnail.png"
import DEV from"./devops-live-thumbnail.png"
import placed from"./dsa-self-paced-thumbnail.png"
import './Navbar.css';


import {FaRegSun } from "react-icons/fa6";

const Navbar=()=>{
 return (
  <>
  <body>
  <nav className="upper-nav">
    <div className="choose">
        
            <select ><option>Courses</option>
            <option>Java</option>
            <option>OOPS</option>
            <option>HTML</option>
            <option>React</option></select>
            <select><option>Tutorials</option>
          </select>
            <select><option>Jobs</option>
            </select>
            <select><option>Practice</option>
            </select>
            <select><option>Contests</option>
            </select>
            
            </div>
            <div className="Glogo">
              <img src={logo} height={20} ></img>
            </div>
<div className="icon">
<input className="searchbar" placeholder="Search here"></input>
<FaSearch size="20px" className="search"></FaSearch>

<FaRegSun size="20px" className="sun"></FaRegSun>
<button className="signinbtn">Sign in</button>


</div>

    

  </nav>
  
  <nav className="lowernav">
    <div className="courses">
      <a href="#" >DSA Classroom Course</a>
      <a href="#" >Trending Now</a>
      <a href="#" >Data Structures</a>
      <a href="#" >Algorithms</a>
      <a href="#" >Interview Preparations</a>
      <a href="#" >Data Science</a>
      <a href="#" >Topic-wise Practice</a>
      <a href="#" >Python</a>
      <a href="#" >Machine Learning</a>
      <a href="#" >JavaScript</a>
      <a href="#" >Java</a>
      <a href="#" >C</a>
      </div> 

  </nav>
  <div className="hero-section">
    <span>
      Hello,What do you want to learn?
    </span>
    </div>
      <div className="geeksearch">
    <input placeholder="GeeksforGeeks Courses"></input><button>Search</button>
  </div>
  <div className="profession">
    <button>Become a Software Tester</button>
    <button>System Design : LLD To HLD</button>
    <button>DSA:Basic To Advanced Course</button>

  </div>
  
  <div className="desc">
    <img src={DSA} height={130} width={200} alt="dsa"></img>
  <div className="dsadesc">
    <span>How to use ChatGPT API in...</span>
    <h6>ChatGpt is a very powerfulchatbot by <br></br>
    OpenAI which uses Natural Language..</h6>
  </div>
    </div>
    <div className="dev">
    <img src={DEV} height={130}width={200} alt="dev"></img>
    <div className="dsadesc">
    <span>How to use ChatGPT API in...</span>
    <h6>ChatGpt is a very powerfulchatbot by <br></br>
    OpenAI which uses Natural Language..</h6>
  </div>
    </div>
    <div className="placed">
    <img src={placed} height={130}width={200}alt="placed"></img>
    <div className="dsadesc">
    <span>How to use ChatGPT API in...</span>
    <h6>ChatGpt is a very powerfulchatbot by <br></br>
    OpenAI which uses Natural Language..</h6>

  </div>
    
    </div>
 

  </body>
  </>
 )
}
export default Navbar