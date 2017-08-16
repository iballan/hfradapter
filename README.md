# HFRAdapter - Easy as it should be :)
==============

Header + Footer + Parallax + Loading + Less Boilerplate code Adapter 


## I tried to make it easier to deal with RecyclerView Adapter

Screenshots:
--------

// SOON

## Usage :


Java:
``` java

	// The adaoter class looks like this:
	public class ParallaxHFRAdapterTest extends ParallaxHFRAdapter<String, MyViewHolder> {

	    public ParallaxHFRAdapterTest(List<String> items) {
	        super(items);
	    }

	    public ParallaxHFRAdapterTest() {}

	    @Override
	    protected void onBindItemViewHolder(MyViewHolder viewHolder, int position, int type) {
	        viewHolder.bind(getItem(position));
	    }

	    @Override
	    protected MyViewHolder viewHolder(View view, int type) {
	        return new MyViewHolder(view);
	    }

	    @Override
	    protected int layoutId(int type) {
	        return R.layout.item_text;
	    }
	}

	// in the activity
    public class MainActivity extends Activity {
		private RecyclerView recyclerView;
		private ParallaxHFRAdapterTest parallaxHfAdapter;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_hfradapter);

	        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
	        recyclerView.setLayoutManager(new ALinearLayoutManager(this));
	        parallaxHfAdapter = new ParallaxHFRAdapterTest(getItems());
	        recyclerView.setAdapter(parallaxHfAdapter);
            
            parallaxHfAdapter.addHeader(this, R.layout.v_header);
	        parallaxHfAdapter.addFooter(this, R.layout.v_footer);
	        parallaxHfAdapter.setLoadingView(this, R.layout.v_loading);
	        parallaxHfAdapter.setParallaxHeader(true);
	        parallaxHfAdapter.setParallaxFooter(true);

	    }
   }
```

Install
--------

You can install using Gradle:

```gradle
	repositories {
	    maven { url "https://jitpack.io" }
	}
	dependencies {
	    compile 'com.github.iballan:hfradapter:1.0.6'
	}
```

### TODO:

- [x] Add Scroll aware RecyclerView
- [ ] Add Screenshots
- [ ] Add Example of Scroll aware RecyclerView 
- [ ] Test AutoLoadMore listener
- [ ] Test Scroll aware RecyclerView (MBRecyclerView) 


Contact me:
--------

Twitter: [@mbh01t](https://twitter.com/mbh01t)

Github: [iballan](https://github.com/iballan)

Website: [www.mbh01.com](http://mbh01.com)

Credits:
--------



License
--------

    Copyright 2017 Mohamad Ballan.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
