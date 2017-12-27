---
layout: default
title: "HMDA Platform API"
class: home
---

<hgroup>
  <h1>The HMDA Platform API <code>v1</code></h1>
  <h2>Help to prepare and test your HMDA data</h2>
  <p class="usa-font-lead">The HMDA Platform API is <strong><abbr title="Cross-Origin Resource Sharing">CORS</abbr></strong> enabled and requires <strong>no authentication</strong>. The API makes it easy to check a single LAR for formatting errors, generate/validate a check digit, calculate rate spread, and more.</p>
</hgroup>

<p>Current status: <span class="usa-label usa-label-success">Up</span></p>

---

<h3>The API URL</h3>
<p>All API endpoints in this documentation are available at the following base URL.</p>
{% highlight bash %}
https://ffiec-api.cfpb.gov/public
{% endhighlight %}

<p>For example, if you wanted to look up an institution by ID you would use the following.</p>

{% highlight bash %}
POST https://ffiec-api.cfpb.gov/public/uli/checkDigit
{% endhighlight %}
<p class="usa-text-small">For more details see the <a href="{{ "/check-digit#generate" | relative_url }}">check digit documentation</a>.</p>