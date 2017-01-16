Twitter Suggestions
==========

Given a Twitter user (and his friend tweets) the program suggest 10 news article that can interest him.

### How do I get set up? ###

The project has been created using Intellij Idea Community Edition. To properly build it you have to:

* Git clone the repository
* Compile the project including all of the JAR present in the API/ folder
  
Al already compiled version is present in the folder out/artifacts/twitter_suggestions_jar/ and it's ready to use.

### How to use? ###

There is already a simple GUI on the main start. All you have to do is to simply enter a @user Twitter account and press the start button.

### Limitations ###

The main limitation came from the [Twitter API limit usage](https://dev.twitter.com/rest/public/rate-limiting). You can have a total of 180 request/15 minutes for listing the friends of a user.  
Friends are those that a user follows and by whom she is followed back.  
So if a user have 1M followers and 200 following we take the minimum list (the following one) and check if every user is also a follower. In this case we hit the limit of 180/req and will wait the necessary time to complete the request from Twitter.  

### License ###

	The MIT License (MIT)

	Copyright (c) 2017 Alessandro Beltramo

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.