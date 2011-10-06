# soy-clj

A library for using Google's Closure Templates in Clojure. For information on Closure Templates, see [Google's project page](http://code.google.com/closure/templates/)

## Usage

To get started, just reference the clj-soy artifact in the dependencies section of your project.clj file: `[clj-soy/clj-soy "0.3.0"]`   
Then, you can write a template in a *.soy file. For documentation on Soy templates, see [Google's documentation](http://code.google.com/closure/templates/docs/commands.html). They look like this:

    {namespace clj_soy.example}

    /**
    * Says hello
    * @param? greeting A word to use for greeting
    * @param name A name
    */
    {template .helloWorld}
      {if not $greeting}
        Hello, {$name}!
      {else}
        {$greeting}, {$name}!
      {/if}
    {/template}

Note that the namespace and documenting comment are both required.

Then, you can easily use your template in Clojure using the functions in the clj-soy/template namspace.

#### build

`build` compiles a template from *.soy files. You can pass it as single *.soy file, a seq of files, or a directory containing *.soy files (which will be recursively loaded). Files can be provided either as strings or `java.io.File` objects. 

#### render

`render` combines a compiled template and data, and returns it. Data can be either be a pre-built data structure genarated by `prepare-data`, or a simple Clojure map. Using pre-built data is faster. Keywords as map keys are automatically converted to strings before calling the template rendering engine.

#### prepare-data

`prepare-data` pre-parses a Clojure map into Closure Template's native format. It's called automatically if you pass a map to `render`, but can also be called independently to pre-position data that will be used multiple times. Converts keyword keys into string keys.

An example:

    (ns clj-soy.example
      (:require [clj-soy.template :as soy]))

    (defn example []
      (let [tpl (soy/build "example.soy")]
        (soy/render tpl
                "clj_soy.example.helloWorld"
                {:name "Luke"
                 :greeting "Bonjour"})))


## Todo

* Add some higher-order convenience functions
* Provide Clojure-level support for internationalization
* Provide Clojure-level support for precompiled javascript template generation

## License

Copyright (C) 2011 Luke VanderHart

Distributed under the Eclipse Public License, the same as Clojure.
